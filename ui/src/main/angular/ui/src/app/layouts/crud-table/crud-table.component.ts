import { Component, Input, OnInit, ViewChild } from "@angular/core";
import * as FileSaver from "file-saver";
import { Table, TableLazyLoadEvent } from "primeng/table";
import { Pageable } from "../../../../modules/lipidcompass-backend-client/model/pageable";
import { paging } from "../../../environments/environment";
import { TableColumn } from "../../_models/table-column";
import { CrudAdapter } from "./crud-adapter";

@Component({
  selector: "app-crud-table",
  templateUrl: "./crud-table.component.html",
  styleUrls: ["./crud-table.component.css"],
})
export class CrudTableComponent implements OnInit {
  @ViewChild("dt1") dataTable: Table | undefined;
  @Input() title: string = "";
  @Input() columns: TableColumn[];
  @Input() filterFields: string[];
  @Input() loading: boolean = true;
  @Input() crudAdapter: CrudAdapter<any, any>;
  _selectedColumns: TableColumn[];

  pagedItems: any[];
  totalElements: number;
  // spread operator to shallow clone
  pageable: Pageable = { ...paging.defaultPageable };
  startingRow: number = 1;

  constructor() {}

  ngOnInit(): void {
    var initialLoadEvent: TableLazyLoadEvent = {
      first: 0,
      rows: this.pageable.size,
    };
    this.loadItems(initialLoadEvent);
  }

  loadItems(event: TableLazyLoadEvent) {
    console.debug("Received event: " + JSON.stringify(event));
    this.loading = true;
    this.pageable.page = event.first / event.rows;
    this.pageable.size = event.rows;
    if (event.sortField) {
      this.pageable.sort = [
        event.sortField + "," + (event.sortOrder == 1 ? "asc" : "desc"),
      ];
    }
    if (event.multiSortMeta) {
      this.pageable.sort = event.multiSortMeta.map((sort) => {
        return sort.field + "," + (sort.order == 1 ? "asc" : "desc");
      });
    }
    this.startingRow = event.first + 1;
    console.debug("Requesting data for page: " + JSON.stringify(this.pageable));
    this.crudAdapter
      .get(this.pageable.page, this.pageable.size, this.pageable.sort)
      .subscribe((pagedModel) => {
        console.debug(JSON.stringify(pagedModel));
        this.pagedItems = this.crudAdapter.extractEntries(pagedModel);
        this.totalElements = pagedModel.page.totalElements;
        if (!this._selectedColumns) {
          this._selectedColumns = this.columns;
        }
        this.loading = false;
      });
  }

  exportExcel() {
    import("xlsx").then((xlsx) => {
      const worksheet = xlsx.utils.json_to_sheet(this.pagedItems);
      const workbook = { Sheets: { data: worksheet }, SheetNames: ["data"] };
      const excelBuffer: any = xlsx.write(workbook, {
        bookType: "xlsx",
        type: "array",
      });
      this.saveAsExcelFile(excelBuffer, "lipidcompass-table");
    });
  }

  saveAsExcelFile(buffer: any, fileName: string): void {
    let EXCEL_TYPE =
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8";
    let EXCEL_EXTENSION = ".xlsx";
    const data: Blob = new Blob([buffer], {
      type: EXCEL_TYPE,
    });
    FileSaver.saveAs(
      data,
      fileName + "_export_" + new Date().getTime() + EXCEL_EXTENSION
    );
  }

  edit(rowData: any) {
    this.crudAdapter.edit(rowData);
  }

  delete(rowData: any) {
    this.crudAdapter.delete(rowData);
  }

  onClick(rowData: any) {
    this.crudAdapter.onClick(rowData);
  }

  // applyFilterGlobal($event: any, stringVal: string) {
  //   this.dataTable?.filterGlobal(
  //     ($event.target as HTMLInputElement).value,
  //     stringVal
  //   );
  // }

  @Input() get selectedColumns(): TableColumn[] {
    return this._selectedColumns;
  }

  set selectedColumns(val: TableColumn[]) {
    //restore original order
    this._selectedColumns = this.columns.filter((col) => val.includes(col));
  }
}
