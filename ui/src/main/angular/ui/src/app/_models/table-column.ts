import { filter } from "lodash";
import { ValueFromArray } from "rxjs";

export class TableColumn {
  field: string;
  header: string;
  filterType: ColumnFilterType = ColumnFilterType.Text;
  visible: boolean;

  content(value: any): any {
    return value;
  }
}

export enum ColumnFilterType {
  Text = "text",
  Numeric = "numeric",
  Date = "date",
  Object = "object",
}
