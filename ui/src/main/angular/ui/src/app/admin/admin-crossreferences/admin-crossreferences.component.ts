import { HttpContext } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { ConfirmationService, MessageService } from "primeng/api";
import { Observable } from "rxjs";
import {
  CrossReference,
  CrossReferenceControllerService,
  EntityModelCrossReference,
  PagedModelEntityModelCrossReference
} from "../../../../modules/lipidcompass-backend-client";
import { CrudAdapter } from "../../layouts/crud-table/crud-adapter";
import { ColumnFilterType, TableColumn } from "../../_models/table-column";

@Component({
  selector: "app-admin-crossreferences",
  templateUrl: "./admin-crossreferences.component.html",
  styleUrls: ["./admin-crossreferences.component.css"],
})
export class AdminCrossReferencesComponent implements OnInit {
  columns: TableColumn[];
  filterFields: string[];
  crudAdapter: CrudAdapter<
    PagedModelEntityModelCrossReference,
    EntityModelCrossReference
  >;

  constructor(
    private controller: CrossReferenceControllerService,
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
      // {
      //   field: "mzTabSummary",
      //   header: "Summary",
      //   filterType: ColumnFilterType.Object,
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
    this.crudAdapter = new CrossReferenceCrudAdapter(
      this.controller,
      this.messageService,
      this.confirmationService
    );
  }
}
class CrossReferenceCrudAdapter extends CrudAdapter<
  PagedModelEntityModelCrossReference,
  EntityModelCrossReference
> {
  constructor(
    private controller: CrossReferenceControllerService,
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
  ): Observable<PagedModelEntityModelCrossReference> {
    return this.controller.get(
      page,
      size,
      sort,
      observe,
      reportProgress,
      options
    );
  }

  extractEntries(entries: any): EntityModelCrossReference[] {
    return <EntityModelCrossReference[]>entries?._embedded?.crossReference;
  }

  saveSingle(
    entity: EntityModelCrossReference,
    observe?: "body",
    reportProgress?: boolean,
    options?: {
      httpHeaderAccept?: "application/hal+json";
      context?: HttpContext;
    }
  ): Observable<EntityModelCrossReference> {
    return this.controller.saveSingle(
      <CrossReference>entity,
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
