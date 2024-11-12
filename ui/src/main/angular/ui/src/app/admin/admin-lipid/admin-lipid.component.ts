import { HttpContext } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { ConfirmationService, MessageService } from "primeng/api";
import { Observable } from "rxjs";
import {
  EntityModelLipid,
  Lipid,
  LipidControllerService,
  PagedModelEntityModelLipid
} from "../../../../modules/lipidcompass-backend-client";
import { CrudAdapter } from "../../layouts/crud-table/crud-adapter";
import { ColumnFilterType, TableColumn } from "../../_models/table-column";

@Component({
  selector: "app-admin-lipid",
  templateUrl: "./admin-lipid.component.html",
  styleUrls: ["./admin-lipid.component.css"],
})
export class AdminLipidComponent implements OnInit {
  columns: TableColumn[];
  filterFields: string[];
  crudAdapter: CrudAdapter<PagedModelEntityModelLipid, EntityModelLipid>;

  constructor(
    private controller: LipidControllerService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit() {
    this.columns = [
      /*{ field: "id", header: "Id", filterType: ColumnFilterType.Text },*/
      {
        field: "normalizedShorthandName",
        header: "Normalized Name",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "lipidLevel",
        header: "Structural Level",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "lipidCategory",
        header: "Category",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "lipidClass",
        header: "Class",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "lipidSpecies",
        header: "Species",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
    ];
    this.filterFields = this.columns.map((value) => {
      return value.field;
    });
    this.crudAdapter = new LipidCrudAdapter(
      this.controller,
      this.messageService,
      this.confirmationService
    );
  }
}
class LipidCrudAdapter extends CrudAdapter<
  PagedModelEntityModelLipid,
  EntityModelLipid
> {
  constructor(
    private controller: LipidControllerService,
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
  ): Observable<PagedModelEntityModelLipid> {
    return this.controller.get(
      page,
      size,
      sort,
      observe,
      reportProgress,
      options
    );
  }

  extractEntries(entries: any): EntityModelLipid[] {
    return <EntityModelLipid[]>entries?._embedded?.lipids;
  }

  saveSingle(
    entity: EntityModelLipid,
    observe?: "body",
    reportProgress?: boolean,
    options?: {
      httpHeaderAccept?: "application/hal+json";
      context?: HttpContext;
    }
  ): Observable<EntityModelLipid> {
    return this.controller.saveSingle(
      <Lipid>entity,
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
    return this.controller.deleteById(
      id,
      observe,
      reportProgress,
      options
    );
  }
}
