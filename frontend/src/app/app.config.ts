import {ApplicationConfig, provideZoneChangeDetection} from '@angular/core';
import {provideRouter, withEnabledBlockingInitialNavigation} from '@angular/router';
import {provideAnimations} from '@angular/platform-browser/animations';
import {provideLoadingBar} from '@ngx-loading-bar/core';
import {provideLoadingBarRouter} from '@ngx-loading-bar/router';
import {provideToastr} from 'ngx-toastr';
import {APP_ROUTES} from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }), // Optional: to optimize change detection
    provideRouter(APP_ROUTES, withEnabledBlockingInitialNavigation()),
    provideAnimations(),
    provideLoadingBar({ latencyThreshold: 0 }), // The request takes more than 0ms to start showing the loading bar
    provideLoadingBarRouter(), // To load according to route change
    provideToastr()
  ]
};
