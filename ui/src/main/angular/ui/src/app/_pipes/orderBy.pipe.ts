import { Pipe, PipeTransform } from '@angular/core';
import { Many, orderBy } from 'lodash';

@Pipe({
  name: 'orderBy'
})
export class OrderByPipe implements PipeTransform {

  transform(value: any, sortBy?: any[], order?: Many<boolean | "asc" | "desc">): any {
    return orderBy(value, sortBy, order?order:"asc");
  }

}
