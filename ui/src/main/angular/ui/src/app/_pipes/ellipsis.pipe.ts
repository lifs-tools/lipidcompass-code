import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
  name: "ellipsis",
})
export class EllipsisPipe implements PipeTransform {
  transform(value: any, args?: any): any {
    if (value === undefined || value === null || value.length <= args) {
      return value;
    }
    const limit = args?Math.max(0, args):50;
    return value.substr(0, Math.min(value.length, limit)) + "...";
  }
}
