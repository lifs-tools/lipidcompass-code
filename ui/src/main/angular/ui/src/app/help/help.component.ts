import { Component, OnInit } from "@angular/core";
import { HomeCard } from "../_models/homecard";

class FaqEntry {
  question: string;
  answer: string;
  constructor(question: string, answer: string) {
    this.question = question;
    this.answer = answer;
  }
}

@Component({
  selector: "app-help",
  templateUrl: "./help.component.html",
  styleUrls: ["./help.component.css"],
})
export class HelpComponent implements OnInit {
  helpCards: HomeCard[] = [
    /*
    <h2>Issue Tracker</h2>
    <p>In order to report bugs or to request new features for the LipidCompass web application or for issues concerning
      study submission, please use our <a href="https://github.com/lifs-tools/lipidcompass/issues" target="_blank">issue
        tracker at GitHub</a>.</p>
    <h2>Source Code</h2>
    <p>The source code of the LipidCompass web application is available at <a
        href="https://github.com/lifs-tools/lipidcompass" target="_blank">GitHub</a>. It is licensed under the <a
        href="https://github.com/lifs-tools/lipidcompass/blob/master/LICENSE" target="_blank">terms of the Apache 2.0
        license</a>.</p>
    <h2>Funding & Support</h2>
    <p>LipidCompass is funded by the <a href="https://www.bmbf.de" target="_blank">German Federal Ministry of Education and Research (BMBF)</a> via grant number XXXXXX as part of the <a href="https://www.denbi.de" target="_blank">German Network for Bioinformatics Infrastructure (de.NBI)</a>. It receives further support from the <a href="https://www.bmbwf.gv.at/" target="_blank">Austrian Federal Ministry of Education, Science and Research (BMBWF)</a>.</p>
    <p>Cloud and computational resources for running of LipidCompass are kindly provided by the <a href="https://cloud.denbi.de/" target="_blank">de.NBI Cloud</a>.</p>
    */
    {
      title: "Bug Reports",
      content:
        "In order to report bugs or to request new features for the LipidCompass web application or for issues concerning study submission, please use our issue tracker at GitHub.",
      link: "https://github.com/lifs-tools/lipidcompass/issues",
      linkType: "external",
      icon: "fa fa-bug",
      linkTitle: "GitHub Issues",
    },
    {
      title: "Source Code",
      content:
        "The source code of the LipidCompass web application is available at GitHub. It is licensed under the terms of the Apache 2.0 license.",
      link: "https://github.com/lifs-tools/lipidcompass",
      linkType: "external",
      icon: "fa fa-code",
      linkTitle: "GitHub Repository",
    },
    {
      title: "Funding & Support",
      content:
        "LipidCompass is funded by the German Federal Ministry of Education and Research (BMBF) via grant number XXXXXX as part of the German Network for Bioinformatics Infrastructure (de.NBI). It receives further support from the Austrian Federal Ministry of Education, Science and Research (BMBWF). Cloud and computational resources for running of LipidCompass are kindly provided by the de.NBI Cloud.",
      link: "",
      linkType: "external",
      icon: "fa fa-handshake",
      linkTitle: "de.NBI",
    },
  ];

  faqEntries: FaqEntry[] = [
    new FaqEntry(
      "What is LipidCompass?",
      "LipidCompass is a web-based platform for the analysis of lipidomics data. It provides access to a comprehensive collection of lipidomics datasets and tools for the analysis and comparison of primarily quantitative lipidomics data."
    ),
    new FaqEntry(
      "How can I access LipidCompass?",
      'LipidCompass is available at <a href="https://lipidcompass.org" target="_blank">https://lipidcompass.org</a>.'
    ),
    new FaqEntry(
      "How can I submit my own lipidomics data to LipidCompass?",
      'You can submit your own lipidomics data to LipidCompass using the submission form available at <a href="https://lipidcompass.org/submit" target="_blank">https://lipidcompass.org/submit</a>.'
    ),
    new FaqEntry(
      "What licenses are the datasets available under?",
      "The licenses of the datasets available in LipidCompass vary. Please refer to the dataset details for information about the license of a specific dataset. We generally aim to provide access to datasets under open licenses such as Creative Commons licenses CC-BY-SA or CC0, but some datasets may be subject to more restrictive licenses. Please note that the licenses of the datasets may affect the ways in which you can use the data and how you need to acknowledge their use. Please cite the original publication associated to the dataset, as provided by LipidCompass. If you have any questions about the licenses of the datasets, please contact the LipidCompass team."
    ),
    new FaqEntry(
      "What license is LipidCompass released under?",
      'The source code of LipidCompass is released under the liberal terms of the <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">Apache License 2.0</a>.'
    ),
    new FaqEntry(
      "How can I access the LipidCompass source code?",
      'The source code of LipidCompass is available at <a href="https://github.com/lifs-tools/lipidcompass" target="_blank">https://github.com/lifs-tools/lipidcompass</a>.'
    ),
    // new FaqEntry("How can I access the LipidCompass API?", "The LipidCompass API is available at https://lipidcompass.org/api. Please note that many API endpoints require authentication using JSON web tokens. For more information, please refer to the LipidCompass API documentation available at https://lipidcompass.org/api-docs."),
    new FaqEntry(
      "How can I contact the LipidCompass team?",
      'You can contact the LipidCompass team using the contact information available at <a href="https://lipidcompass.org/help" target="_blank">https://lipidcompass.org/help</a>.'
    ),
    new FaqEntry(
      "How can I report a bug or request a feature?",
      'You can report a bug or request a feature using our issue tracker available at <a href="https://github.com/lifs-tools/lipidcompass/issues" target="_blank">https://github.com/lifs-tools/lipidcompass/issues</a>.'
    ),
    new FaqEntry(
      "How can I cite LipidCompass?",
      "You can cite LipidCompass using the following reference: LipidCompass: a database for comprehensive lipidomics data comparison and reanalysis. TBD, 2024."
    ),
  ];

  constructor() {}

  ngOnInit() {}

  onLoad(event: any) {}

  onError(event: any) {}
}
