import { Component, Input, OnInit } from "@angular/core";
import { HomeCard } from "../_models/homecard";

@Component({
    selector: "app-home",
    templateUrl: "./home.component.html",
    styleUrls: ["./home.component.css"],
})
export class HomeComponent {
    
    @Input() disabled = false;

    homeCards: HomeCard[];

    constructor() {
        this.homeCards = [
            {
                title: "Explore",
                content:
                    "Lipid Compass associates lipids with quantitative experimental evidence from different sources.",
                link: "/study",
                linkType: "internal",
                linkTitle: "Studies",
                icon: "far fa-compass",
            },
            {
                title: "Compare",
                content:
                    "Lipid Compass allows you to compare quantities across datasets to determine reference ranges.",
                link: "/dataset",
                linkType: "internal",
                linkTitle: "Datasets",
                icon: "fas fa-exchange-alt",
            },
            {
                title: "Help",
                content:
                    "Lipid Compass offers extensive on-line help. But you can also request additional support.",
                link: "/help",
                linkType: "internal",
                linkTitle: "Ask",
                icon: "fa fa-question",
            },
        ].map(
            (item) =>
                <HomeCard>{
                    title: item.title,
                    content: item.content,
                    link: item.link,
                    linkType: item.linkType,
                    linkTitle: item.linkTitle,
                    icon: item.icon,
                }
        );
    }

    // ngOnInit() {}
}
