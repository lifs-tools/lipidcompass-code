import { Component, OnInit } from "@angular/core";
import { MenuItem } from "primeng/api";
import {
  IndexControllerService
} from "../../../modules/lipidcompass-backend-client";
import { JobControllerService } from "../../../modules/lipidcompass-data-importer-client";

@Component({
  selector: "app-admin",
  templateUrl: "./admin.component.html",
  styleUrls: ["./admin.component.css"],
})
export class AdminComponent implements OnInit {
  items: MenuItem[];
  backendEndpoints: MenuItem[];

  constructor(
    private lipidCompassBackend: IndexControllerService,
    private lipidCompassDataImporter: JobControllerService
  ) {
  }

  // resolveEndpoints(link: Link): Map<string, Link> {
  //   link.href
  //   const endpoints = new Map<string, Link>();
  //   for (const [key, value] of Object.entries(representationModel._links)) {
  //     console.debug("Endpoint: " + key+" links to " + JSON.stringify(value));
  //     endpoints.set(key, value);
  //   }
  //   return endpoints;
  // }

  ngOnInit() {
    this.items = [
      {
        label: "Data Import",
        icon: "fas fa-cloud-upload-alt",
        expanded: true,
        items: [
          {
            label: "Submissions",
            icon: "fas fa-cloud-upload-alt",
            routerLink: "dataImport/submissions"
          },
          {
            label: "Jobs",
            icon: "fas fa-tasks",
            routerLink: "dataImport/jobs"
          },
          // {
          //   label: "Study Directories",
          //   icon: "fas fa-sitemap",
          //   routerLink: "dataImport/directories"
          // },
        ]
      },
      {
        label: "Backend",
        icon: "fas fa-server",
        items: [
          {
            label: "Cross References",
            icon: "fas fa-link",
            items: [
              {
                label: "CrossReference",
                icon: "fas fa-edit",
                routerLink: "crossReferences"
              },
              {
                label: "HasCrossReference",
                icon: "fas fa-edit"
              }
            ]
          },
          {
            label: "Controlled Vocabularies",
            icon: "fas fa-language",
            items: [
              {
                label: "ControlledVocabulary",
                icon: "fas fa-edit",
                routerLink: "controlledVocabularies"
              },
              {
                label: "HasCvParent",
                icon: "fas fa-edit"
              }
            ]
          },
          {
            label: "CV Parameters",
            icon: "fas fa-tags",
            items: [
              {
                label: "CvParameter",
                icon: "fas fa-edit",
                routerLink: "cvParameters"
              },
              {
                label: "HasCvParameterReference",
                icon: "fas fa-edit"
              }
            ]
          },
          {
            separator: true
          },
          {
            label: "Studies",
            icon: "fas fa-vials",
            items: [
              {
                label: "Study",
                icon: "fas fa-edit",
                routerLink: "studies"
              }
            ]
          },
          {
            label: "Datasets",
            icon: "pi pi-fw pi-copy",
            items: [
              {
                label: "MzTabResult",
                icon: "fas fa-edit",
                routerLink: "mzTabResults"
              },
              {
                label: "HasMzTabResult",
                icon: "fas fa-edit",
                routerLink: "hasMzTabResults"
              }
            ]
          },
          {
            label: "Lipid Quantities",
            icon: "pi pi-fw pi-chart-bar",
            items: [
              {
                label: "LipidQuantity",
                icon: "fas fa-edit",
                routerLink: "lipidQuantities"
              }
            ]
          },
          {
            separator: true
          },
          {
            label: "Lipids",
            icon: "pi pi-fw pi-share-alt",
            items: [
              {
                label: "Lipid",
                icon: "fas fa-edit",
                routerLink: "lipids"
              }
            ]
          },
          {
            label: "LIPID MAPS",
            icon: "fas fa-map",
            items: [
              {
                label: "LipidMapsEntry",
                icon: "fas fa-edit",
                routerLink: "lipidMapsEntries"
              }
            ]
          },
          {
            label: "SwissLipids",
            icon: "fas fa-database",
            items: [
              {
                label: "SwissLipidsEntry",
                icon: "fas fa-edit",
                routerLink: "swissLipidsEntries"
              }
            ]
          },
          {
            separator: true
          },
          {
            label: "Users",
            icon: "fas fa-users",
            items: [
              {
                label: "User",
                icon: "fas fa-user",
                routerLink: "users"
              }
            ]
          }
        ]
      }
    ];
    // this.lipidCompassBackend.index21().subscribe((representationModel) => {
    //   this.backendEndpoints = this.resolveEndpoints(representationModel);
    // });
    // this.items = [
    //   {
    //     label: "Backend",
    //     icon: "pi pi-pw pi-file",
    //     items: [
    //       {
    //         label: "New",
    //         icon: "pi pi-fw pi-plus",
    //         items: [
    //           { label: "User", icon: "pi pi-fw pi-user-plus" },
    //           { label: "Filter", icon: "pi pi-fw pi-filter" },
    //         ],
    //       },
    //       { label: "Open", icon: "pi pi-fw pi-external-link" },
    //       { separator: true },
    //       { label: "Quit", icon: "pi pi-fw pi-times" },
    //     ],
    //   },
    //   {
    //     label: "Edit",
    //     icon: "pi pi-fw pi-pencil",
    //     items: [
    //       { label: "Delete", icon: "pi pi-fw pi-trash" },
    //       { label: "Refresh", icon: "pi pi-fw pi-refresh" },
    //     ],
    //   },
    //   {
    //     label: "Actions",
    //     icon: "pi pi-fw pi-cog",
    //     items: [
    //       {
    //         label: "Edit",
    //         icon: "pi pi-fw pi-pencil",
    //         items: [
    //           { label: "Save", icon: "pi pi-fw pi-save" },
    //           { label: "Update", icon: "pi pi-fw pi-save" },
    //         ],
    //       },
    //       {
    //         label: "Other",
    //         icon: "pi pi-fw pi-tags",
    //         items: [{ label: "Delete", icon: "pi pi-fw pi-minus" }],
    //       },
    //     ],
    //   },
    // ];
  }
}
