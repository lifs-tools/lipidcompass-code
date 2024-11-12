import { HttpContext } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { ConfirmationService, MessageService } from "primeng/api";
import { Observable } from "rxjs";
import {
  EntityModelStudy,
  PagedModelEntityModelStudy,
  Study,
  StudyControllerService,
} from "../../../../modules/lipidcompass-backend-client";
import { CrudAdapter } from "../../layouts/crud-table/crud-adapter";
import { ColumnFilterType, TableColumn } from "../../_models/table-column";

@Component({
  selector: "app-admin-study",
  templateUrl: "./admin-study.component.html",
  styleUrls: ["./admin-study.component.css"],
})
export class AdminStudyComponent implements OnInit {
  columns: TableColumn[];
  filterFields: string[];
  crudAdapter: CrudAdapter<PagedModelEntityModelStudy, EntityModelStudy>;

  constructor(
    private controller: StudyControllerService,
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
        field: "name",
        header: "Name",
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
    this.crudAdapter = new StudyEntryCrudAdapter(
      this.controller,
      this.messageService,
      this.confirmationService
    );
  }
}
class StudyEntryCrudAdapter extends CrudAdapter<
  PagedModelEntityModelStudy,
  EntityModelStudy
> {
  constructor(
    private controller: StudyControllerService,
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
  ): Observable<PagedModelEntityModelStudy> {
    return this.controller.get(
      page,
      size,
      sort,
      observe,
      reportProgress,
      options
    );
  }

  extractEntries(entries: any): EntityModelStudy[] {
    return <EntityModelStudy[]>entries?._embedded?.studies;
  }

  saveSingle(
    entity: EntityModelStudy,
    observe?: "body",
    reportProgress?: boolean,
    options?: {
      httpHeaderAccept?: "application/hal+json";
      context?: HttpContext;
    }
  ): Observable<EntityModelStudy> {
    return this.controller.saveSingle(
      <Study>entity,
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
