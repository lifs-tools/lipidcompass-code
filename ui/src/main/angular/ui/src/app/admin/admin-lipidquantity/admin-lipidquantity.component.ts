import { HttpContext } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Observable } from 'rxjs';
import { EntityModelLipidQuantity, LipidQuantity, LipidQuantityControllerService, PagedModelEntityModelLipidQuantity } from '../../../../modules/lipidcompass-backend-client';
import { CrudAdapter } from '../../layouts/crud-table/crud-adapter';
import { ColumnFilterType, TableColumn } from '../../_models/table-column';

@Component({
  selector: 'app-admin-lipidquantity',
  templateUrl: './admin-lipidquantity.component.html',
  styleUrls: ['./admin-lipidquantity.component.css']
})
export class AdminLipidquantityComponent  implements OnInit {
  columns: TableColumn[];
  filterFields: string[];
  crudAdapter: CrudAdapter<PagedModelEntityModelLipidQuantity, EntityModelLipidQuantity>;

  constructor(
    private controller: LipidQuantityControllerService,
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
        field: "lipid",
        header: "Lipid",
        filterType: ColumnFilterType.Object,
        content: (value) => value,
        visible: true,
      },
      {
        field: "quantity",
        header: "Quantity",
        filterType: ColumnFilterType.Text,
        content: (value) => value,
        visible: true,
      },
      {
        field: "quantificationUnit",
        header: "Quant Unit",
        filterType: ColumnFilterType.Object,
        content: (value) => value,
        visible: true,
      }
      // {
      //   field: "lipidLevel",
      //   header: "Structural Level",
      //   filterType: ColumnFilterType.Text,
      //   content: (value) => value,
      // },
      // {
      //   field: "lipidCategory",
      //   header: "Category",
      //   filterType: ColumnFilterType.Text,
      //   content: (value) => value,
      // },
      // {
      //   field: "lipidClass",
      //   header: "Class",
      //   filterType: ColumnFilterType.Text,
      //   content: (value) => value,
      // },
      // {
      //   field: "lipidSpecies",
      //   header: "Species",
      //   filterType: ColumnFilterType.Text,
      //   content: (value) => value,
      // },
    ];
    this.filterFields = this.columns.map((value) => {
      return value.field;
    });
    this.crudAdapter = new LipidQuantityCrudAdapter(
      this.controller,
      this.messageService,
      this.confirmationService
    );
  }
}
class LipidQuantityCrudAdapter extends CrudAdapter<
  PagedModelEntityModelLipidQuantity,
  EntityModelLipidQuantity
> {
  constructor(
    private controller: LipidQuantityControllerService,
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
  ): Observable<PagedModelEntityModelLipidQuantity> {
    return this.controller.get(
      page,
      size,
      sort,
      observe,
      reportProgress,
      options
    );
  }

  extractEntries(entries: any): EntityModelLipidQuantity[] {
    return <EntityModelLipidQuantity[]>entries?._embedded?.lipidQuantities;
  }

  saveSingle(
    entity: EntityModelLipidQuantity,
    observe?: "body",
    reportProgress?: boolean,
    options?: {
      httpHeaderAccept?: "application/hal+json";
      context?: HttpContext;
    }
  ): Observable<EntityModelLipidQuantity> {
    return this.controller.saveSingle(
      <LipidQuantity>entity,
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