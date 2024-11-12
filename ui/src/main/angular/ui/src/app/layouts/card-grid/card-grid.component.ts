import { Component, Input, OnInit } from "@angular/core";
import { HomeCard } from "../../_models/homecard";

@Component({
    selector: "app-card-grid",
    templateUrl: "./card-grid.component.html",
    styleUrls: ["./card-grid.component.css"],
})
export class CardGridComponent implements OnInit {
    @Input() cards: HomeCard[];

    constructor() {}

    ngOnInit() {}
}
