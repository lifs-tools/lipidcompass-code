import { HttpContext } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { ConfirmationService, MessageService } from "primeng/api";
import { Observable } from "rxjs";
import {
  EntityModelSwissLipidsEntry, PagedModelEntityModelSwissLipidsEntry,
  SwissLipidsEntry,
  SwissLipidsEntryControllerService
} from "../../../../modules/lipidcompass-backend-client";
import { CrudAdapter } from "../../layouts/crud-table/crud-adapter";
import { ColumnFilterType, TableColumn } from "../../_models/table-column";

@Component({
  selector: "app-admin-swisslipids",
  templateUrl: "./admin-swisslipids.component.html",
  styleUrls: ["./admin-swisslipids.component.css"],
})
export class AdminSwissLipidsComponent implements OnInit {
  columns: TableColumn[];
  filterFields: string[];
  crudAdapter: CrudAdapter<
    PagedModelEntityModelSwissLipidsEntry,
    EntityModelSwissLipidsEntry
  >;

  constructor(
    private controller: SwissLipidsEntryControllerService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit() {
    this.columns = [
      /*{ field: "id", header: "Id", filterType: ColumnFilterType.Text },*/
      {
        field: "id",
        header: "Id",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "transactionUuid",
        header: "Transaction",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "normalizedName",
        header: "Shorthand Name",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "description",
        header: "Description",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "abbreviation",
        header: "SwissLipids Abbr",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "nativeId",
        header: "SwissLipids Id",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "nativeUrl",
        header: "SwissLipids Entry",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "level",
        header: "Level",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      // {
      //   field: "label",
      //   header: "Label",
      //   filterType: ColumnFilterType.Text,
      //   content: (value) => value
      // },
      // {
      //   field: "name",
      //   header: "Name",
      //   filterType: ColumnFilterType.Text,
      //   content: (value) => value
      // },
      // {
      //   field: "version",
      //   header: "Version",
      //   filterType: ColumnFilterType.Text,
      //   content: (value) => value
      // },
      // {
      //   field: "uri",
      //   header: "Uri",
      //   filterType: ColumnFilterType.Text,
      //   content: (value) => value
      // },
      {
        field: "visibility",
        header: "Visibility",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
    ];
    this.filterFields = this.columns.map((value) => {
      return value.field;
    });
    this.crudAdapter = new SwissLipidsEntryCrudAdapter(
      this.controller,
      this.messageService,
      this.confirmationService
    );
  }
}
class SwissLipidsEntryCrudAdapter extends CrudAdapter<
  PagedModelEntityModelSwissLipidsEntry,
  EntityModelSwissLipidsEntry
> {
  constructor(
    private controller: SwissLipidsEntryControllerService,
    messageService: MessageService,
    confirmationService: ConfirmationService
  ) {
    super(messageService, confirmationService);
  }

  get(
    page?: number,
    size?: number,
    sort?: Array<string>,
    observe?: "body",
    reportProgress?: boolean,
    options?: {
      httpHeaderAccept?: "application/hal+json";
      context?: HttpContext;
    }
  ): Observable<PagedModelEntityModelSwissLipidsEntry> {
    return this.controller.get(
      page,
      size,
      sort,
      observe,
      reportProgress,
      options
    );
  }

  extractEntries(entries: any): EntityModelSwissLipidsEntry[] {
    return <EntityModelSwissLipidsEntry[]>(
      entries?._embedded?.swissLipidsEntries
    );
  }

  saveSingle(
    entity: EntityModelSwissLipidsEntry,
    observe?: "body",
    reportProgress?: boolean,
    options?: {
      httpHeaderAccept?: "application/hal+json";
      context?: HttpContext;
    }
  ): Observable<EntityModelSwissLipidsEntry> {
    return this.controller.saveSingle(
      <SwissLipidsEntry>entity,
      observe,
      reportProgress,
      options
    );
  }
  deleteById(
    id: string,
    observe?: "body",
    reportProgress?: boolean,
    options?: {
      httpHeaderAccept?: "application/hal+json";
      context?: HttpContext;
    }
  ): Observable<object> {
    return this.controller.deleteById(id, observe, reportProgress, options);
  }
}
