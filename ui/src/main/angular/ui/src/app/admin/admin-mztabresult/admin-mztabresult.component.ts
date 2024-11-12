import { HttpContext } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { ConfirmationService, MessageService } from "primeng/api";
import { Observable } from "rxjs";
import {
  EntityModelMzTabResult,
  MzTabResult,
  MzTabResultControllerService,
  PagedModelEntityModelMzTabResult,
} from "../../../../modules/lipidcompass-backend-client";
import { CrudAdapter } from "../../layouts/crud-table/crud-adapter";
import { ColumnFilterType, TableColumn } from "../../_models/table-column";

@Component({
  selector: "app-admin-mztabresult",
  templateUrl: "./admin-mztabresult.component.html",
  styleUrls: ["./admin-mztabresult.component.css"],
})
export class AdminMztabresultComponent implements OnInit {
  columns: TableColumn[];
  filterFields: string[];
  crudAdapter: CrudAdapter<
    PagedModelEntityModelMzTabResult,
    EntityModelMzTabResult
  >;

  constructor(
    private controller: MzTabResultControllerService,
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
        field: "mzTabSummary",
        header: "Summary",
        filterType: ColumnFilterType.Object,
        content: (value) => value,
        visible: true,
      },
      {
        field: "submissionStatus",
        header: "Status",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
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
    this.crudAdapter = new MzTabResultCrudAdapter(
      this.controller,
      this.messageService,
      this.confirmationService
    );
  }
}
class MzTabResultCrudAdapter extends CrudAdapter<
  PagedModelEntityModelMzTabResult,
  EntityModelMzTabResult
> {
  constructor(
    private controller: MzTabResultControllerService,
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
  ): Observable<PagedModelEntityModelMzTabResult> {
    return this.controller.get(
      page,
      size,
      sort,
      observe,
      reportProgress,
      options
    );
  }

  extractEntries(entries: any): EntityModelMzTabResult[] {
    return <EntityModelMzTabResult[]>entries?._embedded?.mzTabResults;
  }

  saveSingle(
    entity: EntityModelMzTabResult,
    observe?: "body",
    reportProgress?: boolean,
    options?: {
      httpHeaderAccept?: "application/hal+json";
      context?: HttpContext;
    }
  ): Observable<EntityModelMzTabResult> {
    return this.controller.saveSingle(
      <MzTabResult>entity,
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
