import { Component, Input, OnInit } from "@angular/core";
import { DecimalPipe } from "@angular/common";
import { ActivatedRoute, Router } from "@angular/router";
import {
  DatabaseStatistics,
  StatsControllerService,
} from "../../../../modules/lipidcompass-backend-client";
import {
  animate,
  state,
  style,
  transition,
  trigger,
} from "@angular/animations";
import { StatsCard } from "../../_models/statscard";
import { colorScales } from "../../../environments/environment";
import { defaultColorScales } from "../../../environments/common";

@Component({
  selector: "app-db-stats",
  templateUrl: "./db-stats.component.html",
  styleUrls: ["./db-stats.component.css"],
  animations: [
    trigger("rotatedState", [
      state("default", style({ transform: "rotate(0)" })),
      state("rotated", style({ transform: "rotate(-275deg)" })),
      transition(
        "rotated => default",
        animate("1600ms cubic-bezier(.17,.67,.88,.2)")
      ),
      transition(
        "default => rotated",
        animate("1200ms cubic-bezier(.17,.67,.88,.2)")
      ),
    ]),
  ],
})
export class DbStatsComponent implements OnInit {
  state = "default";
  dbStats: DatabaseStatistics;
  lipidStatsCards: StatsCard[] = [];
  dbStatsCards: StatsCard[] = [];

  loadingStats: Boolean = false;

  rotate() {
    this.state = this.state === "default" ? "rotated" : "default";
  }

  constructor(private statsService: StatsControllerService) {}

  ngOnInit() {
    this.loadingStats = true;
    this.statsService.stats().subscribe(
      (response) => {
        if (response) {
          console.debug(JSON.stringify(response?.statistics));
          this.dbStats = response?.statistics;
          this.lipidStatsCards.push(
            {
              title: "Lipid Categories",
              value: this.dbStats?.categories,
              unit: "",
              link: "",
              linkType: "none",
              icon: "fas fa-dna fa-xl",
              linkTitle: "View Lipid Categories",
              titleCssClasses: "text-secondary text-xl",
              cardBackgroundCssClasses: "[bg-light]",
              cardBackgroundColor: "#FFFFFF",
              cardTextCssClasses: "font-bold text-lg text-white",
              iconBackgroundColor: colorScales.colorbrewer.RdYlBu[8][0],
              iconForegroundColor: "#FFFFFF",
            },
            {
              title: "Lipid Classes",
              value: this.dbStats?.classes,
              unit: "",
              link: "",
              linkType: "none",
              icon: "fas fa-dna fa-xl",
              linkTitle: "View Lipid Classes",
              titleCssClasses: "text-secondary text-xl",
              cardBackgroundCssClasses: "[bg-light]",
              cardBackgroundColor: "#FFFFFF",
              cardTextCssClasses: "font-bold text-lg text-white",
              iconBackgroundColor: colorScales.colorbrewer.RdYlBu[8][1],
              iconForegroundColor: "#FFFFFF",
            },
            {
              title: "Lipid Species",
              value: this.dbStats?.species,
              unit: "",
              link: "",
              linkType: "none",
              icon: "fas fa-dna fa-xl",
              linkTitle: "View Lipid Species",
              titleCssClasses: "text-secondary text-xl",
              cardBackgroundCssClasses: "[bg-light]",
              cardBackgroundColor: "#FFFFFF",
              cardTextCssClasses: "font-bold text-lg text-white",
              iconBackgroundColor: colorScales.colorbrewer.RdYlBu[8][2],
              iconForegroundColor: "#FFFFFF",
            },
            {
              title: "Lipid Molecular Species",
              value: this.dbStats?.molecularSpecies,
              unit: "",
              link: "",
              linkType: "none",
              icon: "fas fa-dna fa-xl",
              linkTitle: "View Lipid Molecular Species",
              titleCssClasses: "text-secondary text-xl",
              cardBackgroundCssClasses: "[bg-light]",
              cardBackgroundColor: "#FFFFFF",
              cardTextCssClasses: "font-bold text-lg text-white",
              iconBackgroundColor: colorScales.colorbrewer.RdYlBu[8][3],
              iconForegroundColor: "#FFFFFF",
            },
            {
              title: "Lipid SN Position",
              value: this.dbStats?.snPosition,
              unit: "",
              link: "",
              linkType: "none",
              icon: "fas fa-dna fa-xl",
              linkTitle: "View Lipid SN Position",
              titleCssClasses: "text-secondary text-xl",
              cardBackgroundCssClasses: "[bg-light]",
              cardBackgroundColor: "#FFFFFF",
              cardTextCssClasses: "font-bold text-lg text-white",
              iconBackgroundColor: colorScales.colorbrewer.RdYlBu[8][4],
              iconForegroundColor: "#FFFFFF",
            },
            {
              title: "Lipid Structure Defined",
              value: this.dbStats?.structureDefined,
              unit: "",
              link: "",
              linkType: "none",
              icon: "fas fa-dna fa-xl",
              linkTitle: "View Lipid Structure Defined",
              titleCssClasses: "text-secondary text-xl",
              cardBackgroundCssClasses: "[bg-light]",
              cardBackgroundColor: "#FFFFFF",
              cardTextCssClasses: "font-bold text-lg text-white",
              iconBackgroundColor: colorScales.colorbrewer.RdYlBu[8][5],
              iconForegroundColor: "#FFFFFF",
            },
            {
              title: "Lipid Full Structure",
              value: this.dbStats?.fullStructure,
              unit: "",
              link: "",
              linkType: "none",
              icon: "fas fa-dna fa-xl",
              linkTitle: "View Lipid Full Structure",
              titleCssClasses: "text-secondary text-xl",
              cardBackgroundCssClasses: "[bg-light]",
              cardBackgroundColor: "#FFFFFF",
              cardTextCssClasses: "font-bold text-lg text-white",
              iconBackgroundColor: colorScales.colorbrewer.RdYlBu[8][6],
              iconForegroundColor: "#FFFFFF",
            },
            {
              title: "Lipid Complete Structure",
              value: this.dbStats?.fullStructure,
              unit: "",
              link: "",
              linkType: "none",
              icon: "fas fa-dna fa-xl",
              linkTitle: "View Lipid Complete Structure",
              titleCssClasses: "text-secondary text-xl",
              cardBackgroundCssClasses: "[bg-light]",
              cardBackgroundColor: "#FFFFFF",
              cardTextCssClasses: "font-bold text-lg text-white",
              iconBackgroundColor: colorScales.colorbrewer.RdYlBu[8][7],
              iconForegroundColor: "#FFFFFF",
            },
          );

          this.dbStatsCards.push(
            // add a card for the number of studies
            {
              title: "Studies",
              value: this.dbStats?.experiments,
              unit: "",
              link: "/study",
              linkType: "internal",
              icon: "fa fa-vials fa-2xl",
              linkTitle: "View Studies",
              titleCssClasses: "text-secondary text-xl",
              cardBackgroundCssClasses: "[bg-light]",
              cardBackgroundColor: "#FFFFFF",
              cardTextCssClasses: "font-bold text-lg text-white",
              iconBackgroundColor: colorScales.colorbrewer.RdYlBu[8][0],
              iconForegroundColor: "#FFFFFF",
            },
            // add a card for the number of datasets
            {
              title: "Datasets",
              value: this.dbStats?.results,
              unit: "",
              link: "/dataset",
              linkType: "internal",
              icon: "fa fa-box-archive fa-2xl",
              linkTitle: "View Datasets",
              titleCssClasses: "text-secondary text-xl",
              cardBackgroundCssClasses: "[bg-light]",
              cardBackgroundColor: "#FFFFFF",
              cardTextCssClasses: "font-bold text-lg text-white",
              iconBackgroundColor: colorScales.colorbrewer.RdYlBu[8][1],
              iconForegroundColor: "#FFFFFF",
            },
            // add a card for the number of biological species
            {
              title: "Biological Species",
              value: this.dbStats?.taxonomicSpecies,
              unit: "",
              link: "/species",
              linkType: "none",
              icon: "icon icon-species icon-mouse fa-2xl",
              linkTitle: "View Biological Species",
              titleCssClasses: "text-secondary text-xl",
              cardBackgroundCssClasses: "[bg-light]",
              cardBackgroundColor: "#FFFFFF",
              cardTextCssClasses: "font-bold text-lg text-white",
              iconBackgroundColor: colorScales.colorbrewer.RdYlBu[8][2],
              iconForegroundColor: "#FFFFFF",
            },
            // add a card for the number of tissues
            {
              title: "Tissues",
              value: this.dbStats?.tissues,
              unit: "",
              link: "/tissue",
              linkType: "none",
              icon: "fa fa-droplet fa-2xl",
              linkTitle: "View Tissues",
              titleCssClasses: "text-secondary text-xl",
              cardBackgroundCssClasses: "[bg-light]",
              cardBackgroundColor: "#FFFFFF",
              cardTextCssClasses: "font-bold text-lg text-white",
              iconBackgroundColor: colorScales.colorbrewer.RdYlBu[8][5],
              iconForegroundColor: "#FFFFFF",
            },
            // add a card for the number of cell types
            {
              title: "Cell Types",
              value: this.dbStats?.cellTypes,
              unit: "",
              link: "/celltype",
              linkType: "none",
              icon: "fa fa-bacteria fa-2xl",
              linkTitle: "View Cell Types",
              titleCssClasses: "text-secondary text-xl",
              cardBackgroundCssClasses: "[bg-light]",
              cardBackgroundColor: "#FFFFFF",
              cardTextCssClasses: "font-bold text-lg text-white",
              iconBackgroundColor: colorScales.colorbrewer.RdYlBu[8][6],
              iconForegroundColor: "#FFFFFF",
            },
            // add a card for the number of diseases
            {
              title: "Disease",
              value: this.dbStats?.diseases,
              unit: "",
              link: "/disease",
              linkType: "none",
              icon: "fa fa-head-side-cough fa-2xl",
              linkTitle: "View Cell Types",
              titleCssClasses: "text-secondary text-xl",
              cardBackgroundCssClasses: "[bg-light]",
              cardBackgroundColor: "#FFFFFF",
              cardTextCssClasses: "font-bold text-lg text-white",
              iconBackgroundColor: colorScales.colorbrewer.RdYlBu[8][7],
              iconForegroundColor: "#FFFFFF",
            }
          );
          this.loadingStats = false;
        }
      },
      (error) => {
        console.error("Error while loading stats: " + JSON.stringify(error));
        this.loadingStats = false;
      }
    );
  }
}
