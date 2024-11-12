import { Component, OnInit } from "@angular/core";
import { ChangeDetectorRef } from "@angular/core";
import { FormGroup, AbstractControl } from "@angular/forms";
import { FormlyFieldConfig, FormlyFormOptions } from "@ngx-formly/core";
import { PublicationItem } from "../../../modules/lipidcompass-backend-client";
import {
  Metadata,
  MzTab,
  SmallMoleculeEvidence,
  SmallMoleculeFeature,
  SmallMoleculeSummary,
} from "../../../modules/mztabm-validator-client";

@Component({
  selector: "app-mztab",
  templateUrl: "./mztab.component.html",
  styleUrls: ["./mztab.component.css"],
})
export class MztabComponent implements OnInit {
  form = new FormGroup({});
  options: FormlyFormOptions = {};
  model = <MzTab>{
    metadata: <Metadata>{},
    smallMoleculeSummary: <Array<SmallMoleculeSummary>>[],
    smallMoleculeFeature: <Array<SmallMoleculeFeature>>[],
    smallMoleculeEvidence: <Array<SmallMoleculeEvidence>>[],
  };
  fields: FormlyFieldConfig[] = [
    {
      fieldGroupClassName: "row",
      fieldGroup: [
        {
          className: "col-6",
          key: "metadata.mzTabVersion",
          type: "input",
          defaultValue: "mzTab-M 1.0.0",
          props: {
            label: "MzTab Version",
            // placeholder: 'Enter email',
            required: true,
            description:
              'The version of the mzTab file. The suffix MUST be "-M" for mzTab for metabolomics (mzTab-M).',
          },
          expressions: {
            "props.disabled": "true",
            hide: "true",
          },
        },
        {
          className: "col-6",
          key: "metadata.mzTabID",
          type: "input",
          defaultValue: "LCDSXXXXXXXXXX",
          props: {
            label: "MzTab ID",
            // placeholder: 'Enter email',
            required: true,
            description:
              "The ID of the mzTab file, this could be supplied by the repository from which it is downloaded or a local identifier from the lab producing the file. It is not intended to be a globally unique ID but carry some locally useful meaning.",
          },
          expressions: {
            "props.disabled": "true",
            hide: "true",
          },
        },
      ],
    },
    {
      className: "col-6",
      key: "metadata.title",
      type: "input",
      // defaultValue: '',
      props: {
        label: "Title",
        placeholder: "The study title",
        required: true,
        description: "The file’s human readable title.",
      },
    },
    {
      className: "col-6",
      key: "metadata.description",
      type: "input",
      // defaultValue: '',
      props: {
        label: "Description",
        placeholder: "The study description",
        required: true,
        description: "The file’s human readable description.",
      },
    },
    {
      fieldGroupClassName: "row",
      fieldGroup: [
        {
          className: "col-6",
          key: "metadata.contact",
          type: "repeat",
          props: {
            addText: "New Contact",
            label: "Contacts",
            // required: true,
            // min: 1
          },
          fieldArray: {
            props: {
              required: true,
              min: 1,
            },
            fieldGroup: [
              {
                key: "name",
                type: "input",
                props: {
                  label: "Name",
                  placeholder: "James Watson",
                  required: true,
                  description: "The contact's name.",
                },
              },
              {
                key: "affiliation",
                type: "input",
                props: {
                  label: "Affiliation",
                  placeholder: "Some University",
                  required: true,
                  description: "The contact's affiliation.",
                },
              },
              {
                key: "email",
                type: "input",
                props: {
                  label: "E-Mail",
                  placeholder: "name@server.org",
                  pattern:
                    "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$",
                  required: true,
                  description: "The contact's e-mail address.",
                },
                validation: {
                  messages: {
                    pattern: (error: any, field: FormlyFieldConfig) =>
                      `"${field.formControl.value}" is not a valid e-mail address`,
                  },
                },
              },
              {
                key: "orcid",
                type: "input",
                props: {
                  label: "Orcid",
                  placeholder: "0000-0000-0000-0000",
                  // pattern: '',
                  required: false,
                  description: "The contact's orcid id, without https prefix.",
                },
                validators: {
                  ip: {
                    expression: (c: AbstractControl) =>
                      !c.value || /^(\d{4}-){3}\d{3}(\d|X)$/.test(c.value),
                    message: (error: any, field: FormlyFieldConfig) =>
                      `"${field.formControl.value}" is not a valid ORCID`,
                  },
                },
              },
            ],
          },
        },
        {
          className: "col-6",
          key: "metadata.publication",
          type: "repeat",
          props: {
            addText: "New Publication",
            label: "Publications",
            // required: true,
            // min: 1
          },
          fieldArray: {
            props: {
              required: false,
              min: 0,
            },
            fieldGroup: [
              {
                key: "id",
                type: "input",
                props: {
                  label: "Id",
                  placeholder: "1",
                  required: false,
                  description: "The publication Id",
                },
                expressions: {
                  hide: "true",
                },
              },
              {
                key: "publicationItems",
                type: "repeat",
                props: {
                  addText: "New Item",
                  label: "Publication Items",
                },
                fieldArray: {
                  props: {
                    required: false,
                    min: 0,
                  },
                  fieldGroup: [
                    {
                      key: "type",
                      type: "select",
                      props: {
                        label: "Type",
                        options: [
                          { value: PublicationItem.TypeEnum.doi, label: "DOI" },
                          {
                            value: PublicationItem.TypeEnum.pubmed,
                            label: "PUBMED ID",
                          },
                          { value: PublicationItem.TypeEnum.uri, label: "URI" },
                        ],
                        required: true,
                        description:
                          "The type qualifier of this publication item.",
                      },
                    },
                    {
                      key: "accession",
                      type: "input",
                      props: {
                        label: "Accession",
                        placeholder: "10.1007/s11306-014-0676-4",
                        // pattern: '',
                        required: true,
                        description:
                          "The native accession id for this publication item.",
                      },
                    },
                  ],
                },
              },
            ],
          },
        },
      ],
    },
    {
      fieldGroupClassName: "row",
      fieldGroup: [
        {
          className: "col-6",
          key: "metadata.uri",
          type: "repeat",
          props: {
            addText: "New URI",
            label: "URIs",
            description:
              "URIs pointing to the file’s source data (e.g., a MetaboLights records) or other related data.",
          },
          fieldArray: {
            props: {
              required: false,
              min: 0,
            },
            fieldGroup: [
              {
                key: "value",
                type: "input",
                props: {
                  label: "URI",
                  placeholder: "https://www.ebi.ac.uk/metabolights/MTBLS1375",
                  pattern:
                    "^(?!mailto:)(?:(?:http|https|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?:(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[0-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})))|localhost)(?::\\d{2,5})?(?:(/|\\?|#)[^\\s]*)?$",
                  required: true,
                  minLength: 1,
                  maxLength: 2082,
                  description:
                    "A URI pointing to the file’s source data (e.g., a MetaboLights records).",
                },
              },
            ],
          },
        },
        {
          className: "col-6",
          key: "metadata.externalStudyUri",
          type: "repeat",
          props: {
            addText: "New External URI",
            label: "External URIs",
            description:
              "URIs pointing to an external file with more details about the study design (e.g., an ISA-TAB file).",
          },
          fieldArray: {
            props: {
              required: false,
              min: 0,
            },
            fieldGroup: [
              {
                key: "value",
                type: "input",
                props: {
                  label: "URI",
                  placeholder: "https://www.ebi.ac.uk/metabolights/MTBLS1375",
                  pattern:
                    "^(?!mailto:)(?:(?:http|https|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?:(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[0-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})))|localhost)(?::\\d{2,5})?(?:(/|\\?|#)[^\\s]*)?$",
                  required: true,
                  minLength: 1,
                  maxLength: 2082,
                  description:
                    "A URI pointing to an external file with more details about the study design (e.g., an ISA-TAB file).",
                },
              },
            ],
          },
        },
      ],
    },
    {
      key: "metadata.instument",
      type: "repeat",
      props: {
        addText: "New Instrument",
        label: "Instruments",
        description:
          "The name, source, analyzer and detector of the instruments used in the experiment. Multiple instruments are numbered [1-n].",
      },
      fieldArray: {
        props: {
          required: true,
          min: 1,
        },
        fieldGroup: [
          {
            key: "id",
            type: "input",
            props: {
              label: "ID",
              placeholder: "1",
              required: true,
              minLength: 1,
              description:
                "The ID identifying the instrument in the mzTab-M file.",
            },
          },
          {
            key: "name",
            type: "input",
            props: {
              label: "Name",
              placeholder: "",
              required: true,
              minLength: 1,
              description:
                "The instrument's name, as defined by the parameter.",
            },
          },
          {
            key: "source",
            type: "input",
            props: {
              label: "Source",
              placeholder: "",
              required: true,
              minLength: 1,
              description:
                "The instrument's source, as defined by the parameter.",
            },
          },
          {
            key: "analyzer",
            type: "input",
            props: {
              label: "Analyzer",
              placeholder: "",
              required: true,
              minLength: 1,
              description:
                "The instrument's mass analyzer, as defined by the parameter.",
            },
          },
          {
            key: "detector",
            type: "input",
            props: {
              label: "Detector",
              placeholder: "",
              required: true,
              minLength: 1,
              description:
                "The instrument's detector, as defined by the parameter.",
            },
          },
        ],
      },
    },
  ];

  constructor() {}

  ngOnInit(): void {
    console.log("MztabComponent init");
  }

  // save(formModel: any): void {
  //   console.debug("FormModel: "+JSON.stringify(formModel));
  // }

  onSubmit(model: any) {
    console.log(model);
  }
}
