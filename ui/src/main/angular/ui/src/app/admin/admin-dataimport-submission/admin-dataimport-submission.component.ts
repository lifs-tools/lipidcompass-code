import { Component, OnInit, ViewChild } from "@angular/core";
import * as FileSaver from "file-saver";
import {
    ConfirmationService,
    LazyLoadEvent,
    MessageService,
} from "primeng/api";
import { ColumnFilter, Table, TableLazyLoadEvent } from "primeng/table";
import {
    EntityModelSubmission,
    FileResource,
    Study,
    SubmissionControllerService,
    User,
} from "../../../../modules/lipidcompass-backend-client";
import { Pageable } from "../../../../modules/lipidcompass-backend-client/model/pageable";
import {
    JobControllerService,
    JobSubmissionInputDto,
} from "../../../../modules/lipidcompass-data-importer-client";
import { paging } from "../../../environments/environment";
import { ColumnFilterType, TableColumn } from "../../_models/table-column";

@Component({
    selector: "app-admin-dataimport-submission",
    templateUrl: "./admin-dataimport-submission.component.html",
    styleUrls: ["./admin-dataimport-submission.component.css"],
})
export class AdminDataimportSubmissionComponent implements OnInit {
    @ViewChild("ads") dataTable: Table | undefined;

    // @Input() tableId: string;
    pagedItems: any[];

    columns: TableColumn[];
    filterFields: string[];

    loading: boolean = true;

    totalElements: number;
    // spread operator to shallow clone
    pageable: Pageable = { ...paging.defaultPageable };
    startingRow: number = 1;

    constructor(
        private submissionController: SubmissionControllerService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private jobControllerService: JobControllerService
    ) {}

    ngOnInit(): void {
        var initialLoadEvent: TableLazyLoadEvent = {
            first: 0,
            rows: this.pageable.size,
        };
        this.loadItems(initialLoadEvent);
        // this.lipidController.get(this.pageable).subscribe((pagedModel) => {
        //   this.pagedItems = pagedModel._embedded.lipids;
        //   /*
        //       id?: string;
        // transactionUuid: string;
        // visibility?: Lipid.VisibilityEnum;
        // revision?: string;
        // dateCreated?: string;
        // dateLastModified?: string;
        // createdBy?: string;
        // updatedBy?: string;
        // nativeUrl?: string;
        // commonName?: string;
        // normalizedShorthandName?: string;
        // lipidLevel?: Lipid.LipidLevelEnum;
        // synonyms?: Array<string>;
        // systematicName?: string;
        // chemicalFormula?: string;
        // exactMass?: number;
        // inchiKey?: string;
        // inchi?: string;
        // smiles?: string;
        // mdlModel?: string;
        // crossReferences?: Array<CrossReference>;
        // lipidCategory?: string;
        // lipidClass?: string;
        // lipidSpecies?: string;
        // lipidMolecularSubspecies?: string;
        // lipidStructuralSubspecies?: string;
        // lipidIsomericSubspecies?: string;
        // nativeId?: string;
        // lipidSpeciesInfo?: FattyAcyl;
        // lipidMapsEntry?: Array<LipidMapsEntry>;
        // swissLipidsEntry?: Array<SwissLipidsEntry>;
        // fattyAcyls?: Array<FattyAcyl>;

        // field: string;
        // header: string;
        // filterType
        // */

        // });
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
        console.debug(
            "Requesting data for page: " + JSON.stringify(this.pageable)
        );
        this.submissionController
            .get(this.pageable.page, this.pageable.size, this.pageable.sort)
            .subscribe((pagedModel) => {
                console.debug(JSON.stringify(pagedModel));
                this.pagedItems = pagedModel?._embedded?.submissions;
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
                        field: "visibility",
                        header: "Visibility",
                        filterType: ColumnFilterType.Text,
                        content: (value) => value,
                        visible: true,
                    },
                    {
                        field: "status",
                        header: "Status",
                        filterType: ColumnFilterType.Text,
                        content: (value) => value,
                        visible: true,
                    },
                    {
                        field: "submitter",
                        header: "Submitter",
                        filterType: ColumnFilterType.Object,
                        content: (value) => {
                            let user: User = <User>value;
                            return (
                                user?.firstName +
                                " " +
                                user?.familyName +
                                "(" +
                                user?.userName +
                                ")"
                            );
                        },
                        visible: true,
                    },
                    {
                        field: "submittedFiles",
                        header: "Submitted Files",
                        filterType: ColumnFilterType.Object,
                        content: (value) => {
                            if (value) {
                                let fileResources: FileResource[] = <
                                    FileResource[]
                                >value;
                                return fileResources
                                    .map(
                                        (fileResource) =>
                                            fileResource?.fileName +
                                            "(" +
                                            fileResource?.fileType +
                                            ")"
                                    )
                                    .join(", ");
                            } else {
                                return "";
                            }
                        },
                        visible: true,
                    },
                    {
                        field: "storageBucket",
                        header: "Storage Bucket",
                        filterType: ColumnFilterType.Text,
                        content: (value) => value,
                        visible: true,
                    },
                    {
                        field: "study",
                        header: "Study",
                        filterType: ColumnFilterType.Object,
                        content: (value) => {
                            let study: Study = <Study>value;
                            return study?.name + " " + study?.description;
                        },
                        visible: true,
                    },
                    {
                        field: "privateLinkUuid",
                        header: "Private Link",
                        filterType: ColumnFilterType.Text,
                        content: (value) => value,
                        visible: true,
                    },
                    {
                        field: "dateCreated",
                        header: "Date Created",
                        filterType: ColumnFilterType.Date,
                        content: (value) => value,
                        visible: true,
                    },
                    {
                        field: "dateLastModified",
                        header: "Date Last Modified",
                        filterType: ColumnFilterType.Date,
                        content: (value) => value,
                        visible: true,
                    },
                ];
                this.filterFields = this.columns.map((value) => {
                    return value.field;
                });
                this.totalElements = pagedModel.page.totalElements;
                this.loading = false;
            });
    }

    reload() {
        var initialLoadEvent: TableLazyLoadEvent = {
            first: 0,
            rows: this.pageable.size,
        };
        this.loadItems(initialLoadEvent);
    }

    import(rowData: EntityModelSubmission) {
        console.debug("import: " + JSON.stringify(rowData));
        this.confirmationService.confirm({
            message:
                "Are you sure you want to start import of " + rowData.id + "?",
            // header: "Confirm",
            // icon: "pi pi-exclamation-triangle",
            accept: () => {
                var jobSubmission = <JobSubmissionInputDto>{
                    maxRows: -1,
                    submissionId: rowData.id,
                };
                this.jobControllerService
                    .startSubmissionImportJob(jobSubmission)
                    .subscribe(
                        (response) => {
                            this.messageService.add({
                                severity: "success",
                                summary: "Successful",
                                detail: "Sent " + rowData.id + " to import",
                                life: 3000,
                            });
                            this.reload();
                        },
                        (error) => {
                            this.messageService.add({
                                severity: "danger",
                                summary: "Failed",
                                detail:
                                    "Failed to send " +
                                    rowData.id +
                                    " to import",
                                life: 3000,
                            });
                            this.reload();
                        }
                    );
            },
        });
    }

    edit(rowData: EntityModelSubmission) {
        console.debug("edit: " + JSON.stringify(rowData));
        rowData.status = "IN_CURATION";
        this.submissionController.saveSingle(rowData).subscribe(
            (response) => {
                this.messageService.add({
                    severity: "success",
                    summary: "Successful",
                    detail: "Submission edited",
                    life: 3000,
                });
                this.reload();
            },
            (error) => {
                this.messageService.add({
                    severity: "danger",
                    summary: "Failed",
                    detail: "Failed to edid " + rowData.id,
                    life: 3000,
                });
                this.reload();
            }
        );
    }

    delete(rowData: EntityModelSubmission) {
        console.debug("delete: " + JSON.stringify(rowData));
        this.confirmationService.confirm({
            message: "Are you sure you want to delete " + rowData.id + "?",
            // header: "Confirm",
            // icon: "pi pi-exclamation-triangle",
            accept: () => {
                this.submissionController.deleteById(rowData.id).subscribe(
                    (response) => {
                        this.messageService.add({
                            severity: "success",
                            summary: "Successful",
                            detail: "Deleted " + rowData.id,
                            life: 3000,
                        });
                        this.reload();
                    },
                    (error) => {
                        this.messageService.add({
                            severity: "danger",
                            summary: "Failed",
                            detail: "Failed to delete " + rowData.id,
                            life: 3000,
                        });
                        this.reload();
                    }
                );
            },
        });
    }

    exportExcel() {
        import("xlsx").then((xlsx) => {
            const worksheet = xlsx.utils.json_to_sheet(this.pagedItems);
            const workbook = {
                Sheets: { data: worksheet },
                SheetNames: ["data"],
            };
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

    applyFilterGlobal($event: any, stringVal: string) {
        this.dataTable?.filterGlobal(
            ($event.target as HTMLInputElement).value,
            stringVal
        );
    }
}
