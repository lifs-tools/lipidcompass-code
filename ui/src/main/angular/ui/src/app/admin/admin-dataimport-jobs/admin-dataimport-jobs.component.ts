import { HttpContext } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { ConfirmationService, Message, MessageService } from "primeng/api";
import { Observable, of } from "rxjs";
import {
  EntityModelJobDto,
  JobControllerService,
  PagedModelEntityModelJobDto,
} from "../../../../modules/lipidcompass-data-importer-client";
import { CrudAdapter } from "../../layouts/crud-table/crud-adapter";
import { ColumnFilterType, TableColumn } from "../../_models/table-column";

@Component({
  selector: "app-admin-dataimport-jobs",
  templateUrl: "./admin-dataimport-jobs.component.html",
  styleUrls: ["./admin-dataimport-jobs.component.css"],
})
export class AdminDataimportJobsComponent implements OnInit {
  columns: TableColumn[];
  filterFields: string[];
  crudAdapter: CrudAdapter<PagedModelEntityModelJobDto, EntityModelJobDto>;
  jobs: EntityModelJobDto[];

  constructor(
    private controller: JobControllerService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit() {
    this.columns = [
      {
        field: "name",
        header: "Name",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "jobInstances",
        header: "Job Instances",
        filterType: ColumnFilterType.Object,
        content: (value) => value,
        visible: true,
      },
    ];
    this.filterFields = this.columns.map((value) => {
      return value.field;
    });
    this.crudAdapter = new JobDtoCrudAdapter(
      this.controller,
      this.messageService,
      this.confirmationService
    );
  }
}

class JobDtoCrudAdapter extends CrudAdapter<PagedModelEntityModelJobDto, EntityModelJobDto> {
  constructor(
    private controller: JobControllerService,
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
  ): Observable<PagedModelEntityModelJobDto> {
    return this.controller.getJobs(
      page,
      size,
      sort,
      observe,
      reportProgress,
      options
    );
  }

  extractEntries(entries: any): EntityModelJobDto[] {
    return entries?._embedded?.jobDtoes;
  }

  saveSingle(
    entity: EntityModelJobDto,
    observe?: "body",
    reportProgress?: boolean,
    options?: {
      httpHeaderAccept?: "application/hal+json";
      context?: HttpContext;
    }
  ): Observable<EntityModelJobDto> {
    //return empty observables
    this.messageService.add(<Message>{
      severity: "info",
      summary: "Save not possible",
      detail: "Operation not supported",
      life: 3000,
    });
    return of({});
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
    this.messageService.add(<Message>{
      severity: "info",
      summary: "Delete not possible",
      detail: "Operation not supported",
      life: 3000,
    });
    return of({});
  }
}
