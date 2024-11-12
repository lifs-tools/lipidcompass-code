import { HttpContext } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { ConfirmationService, MessageService } from "primeng/api";
import { Observable } from "rxjs";
import { EntityModelLipidMapsEntry, LipidMapsEntry, LipidMapsEntryControllerService, PagedModelEntityModelLipidMapsEntry } from "../../../../modules/lipidcompass-backend-client";
import { CrudAdapter } from "../../layouts/crud-table/crud-adapter";
import { ColumnFilterType, TableColumn } from "../../_models/table-column";

@Component({
  selector: "app-admin-lipidmaps",
  templateUrl: "./admin-lipidmaps.component.html",
  styleUrls: ["./admin-lipidmaps.component.css"],
})
export class AdminLipidMapsComponent implements OnInit {
  columns: TableColumn[];
  filterFields: string[];
  crudAdapter: CrudAdapter<
    PagedModelEntityModelLipidMapsEntry,
    EntityModelLipidMapsEntry
  >;

  constructor(
    private controller: LipidMapsEntryControllerService,
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
        field: "name",
        header: "Name",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "systematicName",
        header: "Systematic Name",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "abbreviation",
        header: "LIPID MAPS Abbr",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "lmClassificationCode",
        header: "LIPID MAPS Class",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "nativeId",
        header: "LIPID MAPS Id",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "nativeUrl",
        header: "LIPID MAPS Entry",
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
    this.crudAdapter = new LipidMapsEntryCrudAdapter(
      this.controller,
      this.messageService,
      this.confirmationService
    );
  }
}
class LipidMapsEntryCrudAdapter extends CrudAdapter<
  PagedModelEntityModelLipidMapsEntry,
  EntityModelLipidMapsEntry
> {
  constructor(
    private controller: LipidMapsEntryControllerService,
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
  ): Observable<PagedModelEntityModelLipidMapsEntry> {
    return this.controller.get(
      page,
      size,
      sort,
      observe,
      reportProgress,
      options
    );
  }

  extractEntries(entries: any): EntityModelLipidMapsEntry[] {
    return <EntityModelLipidMapsEntry[]>entries?._embedded?.lipidMapsEntries;
  }

  saveSingle(
    entity: EntityModelLipidMapsEntry,
    observe?: "body",
    reportProgress?: boolean,
    options?: {
      httpHeaderAccept?: "application/hal+json";
      context?: HttpContext;
    }
  ): Observable<EntityModelLipidMapsEntry> {
    return this.controller.saveSingle(
      <LipidMapsEntry>entity,
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
