import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {LoadingBarModule} from '@ngx-loading-bar/core';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LoadingBarModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {}
