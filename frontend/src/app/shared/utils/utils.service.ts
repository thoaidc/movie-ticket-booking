import { Injectable } from '@angular/core';
import dayjs from 'dayjs/esm';

@Injectable({
  providedIn: 'root',
})
export class UtilsService {

  convertToDateString(dateString: string, toFormat: string): string {
    const dateFormats = ['MM-DD-YYYY', 'YYYY-MM-DD', 'DD-MM-YYYY', 'YYYY/MM/DD', 'DD/MM/YYYY'];
    const timeFormat = 'HH:mm:ss';
    let date;

    // Check if dateString matches time format
    if (dayjs(dateString, timeFormat, true).isValid()) {
      date = dayjs(dateString, timeFormat, true);
    } else {
      // Check if dateString matches date formats
      date = dayjs(dateString, dateFormats, true);
    }

    return date.isValid() ? date.format(toFormat) : '';
  }

  getFromToMoment(date?: dayjs.Dayjs, isMaxDate?: boolean): any {
    if (date && Object.keys(date).length !== 0) {
      const date1 = dayjs(date);

      return {
        year: date1.year(),
        month: date1.month() + 1,
        day: date1.date(),
      };
    }

    const _date = isMaxDate ? null : dayjs();
    return _date ? { year: _date.year(), month: _date.month() + 1, day: _date.date() } : null;
  }

  getCurrentDate() {
    const _date = dayjs();
    return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
  }
}
