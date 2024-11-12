import { HttpContext } from "@angular/common/http";
import { ConfirmationService, MessageService } from "primeng/api";
import { Observable } from "rxjs";

export abstract class CrudAdapter<T, R> {
  constructor(
    protected messageService: MessageService,
    protected confirmationService: ConfirmationService
  ) {}

  abstract get(
    page?: number,
    size?: number,
    sort?: Array<string>,
    observe?: "body",
    reportProgress?: boolean,
    options?: {
      httpHeaderAccept?: "application/hal+json";
      context?: HttpContext;
    }
  ): Observable<T>;

  abstract saveSingle(
    entity: R,
    observe?: "body",
    reportProgress?: boolean,
    options?: {
      httpHeaderAccept?: "application/hal+json";
      context?: HttpContext;
    }
  ): Observable<R>;

  abstract deleteById(
    id: string,
    observe?: "body",
    reportProgress?: boolean,
    options?: {
      httpHeaderAccept?: "application/hal+json";
      context?: HttpContext;
    }
  ): Observable<object>;

  abstract extractEntries(entries: any): R[];

  edit(rowEntry: any): void {
    if (rowEntry) {
      console.debug("edit: " + JSON.stringify(rowEntry));
      this.saveSingle(rowEntry).subscribe(
        (response) => {
          this.messageService.add({
            severity: "success",
            summary: "Successful",
            detail: "Submission edited",
            life: 3000,
          });
        },
        (error) => {
          this.messageService.add({
            severity: "danger",
            summary: "Failed",
            detail: "Failed to edit id: " + rowEntry?.id,
            life: 3000,
          });
        }
      );
    }
  }

  delete(rowEntry: any): void {
    if (rowEntry) {
      console.debug("delete: " + JSON.stringify(rowEntry));
      this.confirmationService.confirm({
        message: "Are you sure you want to delete " + rowEntry.id + "?",
        // header: "Confirm",
        // icon: "pi pi-exclamation-triangle",
        accept: () => {
          this.deleteById(rowEntry.id).subscribe(
            (response) => {
              this.messageService.add({
                severity: "success",
                summary: "Successful",
                detail: "Deleted " + rowEntry.id,
                life: 3000,
              });
            },
            (error) => {
              this.messageService.add({
                severity: "danger",
                summary: "Failed",
                detail: "Failed to delete " + rowEntry.id,
                life: 3000,
              });
            }
          );
        },
      });
    }
  }

  onClick(rowEntry: any): void {}
}
