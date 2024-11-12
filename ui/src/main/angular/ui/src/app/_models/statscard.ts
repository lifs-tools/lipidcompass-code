export interface StatsCard {
    title: string;
    value: number;
    unit: string;
    link: string;
    linkType: "external" | "internal" | "none";
    icon: string;
    linkTitle: string;
    titleCssClasses: string;
    cardBackgroundCssClasses: string;
    cardBackgroundColor: string;
    cardTextCssClasses: string;
    iconBackgroundColor: string;
    iconForegroundColor: string;
}