import { CvParameter } from "./cvparameter";
import { MzTabResult } from "./mztabresult";
import { Page, PageAndSort } from "./page";

export interface LipidQuery {
    lipidLevel: string;
    shorthandLipidNames: string[];
    matchingMode: string;
    normalizeName: boolean;
    addDefaultFacets: boolean;
    unit: string;
    mzRange: number[];
    quantityRange: number[];
    mzTabResults: MzTabResult[];
    organisms: CvParameter[];
    tissues: CvParameter[];
    diseases: CvParameter[];
    cellTypes: CvParameter[];
    goTerms: CvParameter[];
    pageAndSort: PageAndSort;
}

// {
//     "content": [
//       {
//         "id": "a4646a5b-419b-3bfb-925f-e92e38edf695",
//         "collection": "lipidQuantity",
//         "transactionUuid": "solr-106",
//         "normalizedName": [
//           "PC 36:6"
//         ],
//         "lipidLevel": [
//           "SPECIES"
//         ],
//         "lipidCategory": [
//           "GP"
//         ],
//         "lipidClass": [
//           "PC"
//         ],
//         "lipidSpecies": [
//           "PC 36:6"
//         ],
//         "lipidMolecularSubSpecies": null,
//         "lipidStructuralSubSpecies": null,
//         "lipidIsomericSubSpecies": null,
//         "organismNames": [
//           "Homo sapiens"
//         ],
//         "organismAccessions": [
//           "NCBITaxon:9606"
//         ],
//         "tissueNames": [
//           "blood plasma"
//         ],
//         "tissueAccessions": [
//           "BTO:0000131"
//         ],
//         "cellTypeNames": null,
//         "cellTypeAccessions": null,
//         "diseaseNames": null,
//         "diseaseAccessions": null,
//         "sampleCustomNames": [
//           "Institutional Review Board",
//           "Current Smoker",
//           "Performing Laboratory",
//           "Age",
//           "Gender",
//           "Body Mass Index"
//         ],
//         "sampleCustomAccessions": [
//           "NCIT:C16741",
//           "NCIT:C67147",
//           "NCIT:C64206",
//           "NCIT:C25150",
//           "NCIT:C17357",
//           "NCIT:C16358"
//         ],
//         "sampleCustomValues": [
//           "National Health Group Institutional Review Board (IRB 10-434), Singapore",
//           "1.0",
//           "Baker IDI, Melbourne",
//           "60.0",
//           "Male",
//           "15.05"
//         ],
//         "assayCustomNames": null,
//         "assayCustomAccessions": null,
//         "assayCustomValues": null,
//         "goMolecularFunctionNames": null,
//         "goMolecularFunctionAccessions": null,
//         "goCellComponentNames": null,
//         "goCellComponentAccessions": null,
//         "goBiologicalProcessNames": null,
//         "goBiologicalProcessAccessions": null,
//         "mzTabResultId": "LCDS1",
//         "smlId": 70,
//         "assayId": 230,
//         "studyVariableId": 2,
//         "assayQuantity": 219.38371,
//         "quantificationUnitAccession": "UO:0000072",
//         "quantificationUnitName": "picomolal",
//         "identificationReliability": "3",
//         "bestIdentificationConfidenceMeasureAccession": "MS:1002890",
//         "bestIdentificationConfidenceMeasureName": "fragmentation score",
//         "bestIdConfidenceValue": 1,
//         "studyVariableFactorNativeIds": null,
//         "studyVariableFactorParameterAccessions": [
//           "NCIT:C16564"
//         ],
//         "studyVariableFactorParameterNames": [
//           "Ethnic Group"
//         ],
//         "studyVariableFactorValues": [
//           "Chinese"
//         ],
//         "studyVariableFactorUnits": null,
//         "studyVariableFactorUnitAccessions": null,
//         "createdBy": "nilshoffmann",
//         "updatedBy": "nilshoffmann",
//         "dateCreated": "2021-03-17T16:27:53.000+00:00",
//         "dateLastModified": "2021-03-17T16:27:53.000+00:00",
//         "revision": "_cCCBzvi--0",
//         "visibility": "PUBLIC",
//         "owners": [
//           "root",
//           "nilshoffmann"
//         ],
//         "score": 1
//       },
//       {
//         "id": "587125c9-a001-3c2f-85c3-c92c4b613b97",
//         "collection": "lipidQuantity",
//         "transactionUuid": "solr-106",
//         "normalizedName": [
//           "PC 36:6"
//         ],
//         "lipidLevel": [
//           "SPECIES"
//         ],
//         "lipidCategory": [
//           "GP"
//         ],
//         "lipidClass": [
//           "PC"
//         ],
//         "lipidSpecies": [
//           "PC 36:6"
//         ],
//         "lipidMolecularSubSpecies": null,
//         "lipidStructuralSubSpecies": null,
//         "lipidIsomericSubSpecies": null,
//         "organismNames": [
//           "Homo sapiens"
//         ],
//         "organismAccessions": [
//           "NCBITaxon:9606"
//         ],
//         "tissueNames": [
//           "blood plasma"
//         ],
//         "tissueAccessions": [
//           "BTO:0000131"
//         ],
//         "cellTypeNames": null,
//         "cellTypeAccessions": null,
//         "diseaseNames": null,
//         "diseaseAccessions": null,
//         "sampleCustomNames": [
//           "Institutional Review Board",
//           "Performing Laboratory",
//           "Current Smoker",
//           "Body Mass Index",
//           "Age",
//           "Gender"
//         ],
//         "sampleCustomAccessions": [
//           "NCIT:C16741",
//           "NCIT:C64206",
//           "NCIT:C67147",
//           "NCIT:C16358",
//           "NCIT:C25150",
//           "NCIT:C17357"
//         ],
//         "sampleCustomValues": [
//           "National Health Group Institutional Review Board (IRB 10-434), Singapore",
//           "Baker IDI, Melbourne",
//           "0.0",
//           "31.03",
//           "47.0",
//           "Female"
//         ],
//         "assayCustomNames": null,
//         "assayCustomAccessions": null,
//         "assayCustomValues": null,
//         "goMolecularFunctionNames": null,
//         "goMolecularFunctionAccessions": null,
//         "goCellComponentNames": null,
//         "goCellComponentAccessions": null,
//         "goBiologicalProcessNames": null,
//         "goBiologicalProcessAccessions": null,
//         "mzTabResultId": "LCDS1",
//         "smlId": 70,
//         "assayId": 231,
//         "studyVariableId": 3,
//         "assayQuantity": 397.35538,
//         "quantificationUnitAccession": "UO:0000072",
//         "quantificationUnitName": "picomolal",
//         "identificationReliability": "3",
//         "bestIdentificationConfidenceMeasureAccession": "MS:1002890",
//         "bestIdentificationConfidenceMeasureName": "fragmentation score",
//         "bestIdConfidenceValue": 1,
//         "studyVariableFactorNativeIds": null,
//         "studyVariableFactorParameterAccessions": [
//           "NCIT:C16564"
//         ],
//         "studyVariableFactorParameterNames": [
//           "Ethnic Group"
//         ],
//         "studyVariableFactorValues": [
//           "Malay"
//         ],
//         "studyVariableFactorUnits": null,
//         "studyVariableFactorUnitAccessions": null,
//         "createdBy": "nilshoffmann",
//         "updatedBy": "nilshoffmann",
//         "dateCreated": "2021-03-17T16:27:53.000+00:00",
//         "dateLastModified": "2021-03-17T16:27:53.000+00:00",
//         "revision": "_cCCBzvi--2",
//         "visibility": "PUBLIC",
//         "owners": [
//           "root",
//           "nilshoffmann"
//         ],
//         "score": 1
//       },
//       {
//         "id": "6950d9f4-482e-3ab9-aa66-ac0ded2a27fd",
//         "collection": "lipidQuantity",
//         "transactionUuid": "solr-106",
//         "normalizedName": [
//           "PC 36:6"
//         ],
//         "lipidLevel": [
//           "SPECIES"
//         ],
//         "lipidCategory": [
//           "GP"
//         ],
//         "lipidClass": [
//           "PC"
//         ],
//         "lipidSpecies": [
//           "PC 36:6"
//         ],
//         "lipidMolecularSubSpecies": null,
//         "lipidStructuralSubSpecies": null,
//         "lipidIsomericSubSpecies": null,
//         "organismNames": [
//           "Homo sapiens"
//         ],
//         "organismAccessions": [
//           "NCBITaxon:9606"
//         ],
//         "tissueNames": [
//           "blood plasma"
//         ],
//         "tissueAccessions": [
//           "BTO:0000131"
//         ],
//         "cellTypeNames": null,
//         "cellTypeAccessions": null,
//         "diseaseNames": null,
//         "diseaseAccessions": null,
//         "sampleCustomNames": [
//           "Institutional Review Board",
//           "Performing Laboratory",
//           "Current Smoker",
//           "Age",
//           "Body Mass Index",
//           "Gender"
//         ],
//         "sampleCustomAccessions": [
//           "NCIT:C16741",
//           "NCIT:C64206",
//           "NCIT:C67147",
//           "NCIT:C25150",
//           "NCIT:C16358",
//           "NCIT:C17357"
//         ],
//         "sampleCustomValues": [
//           "National Health Group Institutional Review Board (IRB 10-434), Singapore",
//           "Baker IDI, Melbourne",
//           "0.0",
//           "53.0",
//           "23.21",
//           "Female"
//         ],
//         "assayCustomNames": null,
//         "assayCustomAccessions": null,
//         "assayCustomValues": null,
//         "goMolecularFunctionNames": null,
//         "goMolecularFunctionAccessions": null,
//         "goCellComponentNames": null,
//         "goCellComponentAccessions": null,
//         "goBiologicalProcessNames": null,
//         "goBiologicalProcessAccessions": null,
//         "mzTabResultId": "LCDS1",
//         "smlId": 70,
//         "assayId": 232,
//         "studyVariableId": 2,
//         "assayQuantity": 557.05853,
//         "quantificationUnitAccession": "UO:0000072",
//         "quantificationUnitName": "picomolal",
//         "identificationReliability": "3",
//         "bestIdentificationConfidenceMeasureAccession": "MS:1002890",
//         "bestIdentificationConfidenceMeasureName": "fragmentation score",
//         "bestIdConfidenceValue": 1,
//         "studyVariableFactorNativeIds": null,
//         "studyVariableFactorParameterAccessions": [
//           "NCIT:C16564"
//         ],
//         "studyVariableFactorParameterNames": [
//           "Ethnic Group"
//         ],
//         "studyVariableFactorValues": [
//           "Chinese"
//         ],
//         "studyVariableFactorUnits": null,
//         "studyVariableFactorUnitAccessions": null,
//         "createdBy": "nilshoffmann",
//         "updatedBy": "nilshoffmann",
//         "dateCreated": "2021-03-17T16:27:53.000+00:00",
//         "dateLastModified": "2021-03-17T16:27:53.000+00:00",
//         "revision": "_cCCBzvi--4",
//         "visibility": "PUBLIC",
//         "owners": [
//           "root",
//           "nilshoffmann"
//         ],
//         "score": 1
//       },
//       {
//         "id": "623c0397-43c1-3db7-b994-5549e555076b",
//         "collection": "lipidQuantity",
//         "transactionUuid": "solr-106",
//         "normalizedName": [
//           "PC 36:6"
//         ],
//         "lipidLevel": [
//           "SPECIES"
//         ],
//         "lipidCategory": [
//           "GP"
//         ],
//         "lipidClass": [
//           "PC"
//         ],
//         "lipidSpecies": [
//           "PC 36:6"
//         ],
//         "lipidMolecularSubSpecies": null,
//         "lipidStructuralSubSpecies": null,
//         "lipidIsomericSubSpecies": null,
//         "organismNames": [
//           "Homo sapiens"
//         ],
//         "organismAccessions": [
//           "NCBITaxon:9606"
//         ],
//         "tissueNames": [
//           "blood plasma"
//         ],
//         "tissueAccessions": [
//           "BTO:0000131"
//         ],
//         "cellTypeNames": null,
//         "cellTypeAccessions": null,
//         "diseaseNames": null,
//         "diseaseAccessions": null,
//         "sampleCustomNames": [
//           "Performing Laboratory",
//           "Institutional Review Board",
//           "Current Smoker",
//           "Body Mass Index",
//           "Age",
//           "Gender"
//         ],
//         "sampleCustomAccessions": [
//           "NCIT:C64206",
//           "NCIT:C16741",
//           "NCIT:C67147",
//           "NCIT:C16358",
//           "NCIT:C25150",
//           "NCIT:C17357"
//         ],
//         "sampleCustomValues": [
//           "Baker IDI, Melbourne",
//           "National Health Group Institutional Review Board (IRB 10-434), Singapore",
//           "1.0",
//           "22.91",
//           "62.0",
//           "Female"
//         ],
//         "assayCustomNames": null,
//         "assayCustomAccessions": null,
//         "assayCustomValues": null,
//         "goMolecularFunctionNames": null,
//         "goMolecularFunctionAccessions": null,
//         "goCellComponentNames": null,
//         "goCellComponentAccessions": null,
//         "goBiologicalProcessNames": null,
//         "goBiologicalProcessAccessions": null,
//         "mzTabResultId": "LCDS1",
//         "smlId": 70,
//         "assayId": 233,
//         "studyVariableId": 3,
//         "assayQuantity": 352.3988,
//         "quantificationUnitAccession": "UO:0000072",
//         "quantificationUnitName": "picomolal",
//         "identificationReliability": "3",
//         "bestIdentificationConfidenceMeasureAccession": "MS:1002890",
//         "bestIdentificationConfidenceMeasureName": "fragmentation score",
//         "bestIdConfidenceValue": 1,
//         "studyVariableFactorNativeIds": null,
//         "studyVariableFactorParameterAccessions": [
//           "NCIT:C16564"
//         ],
//         "studyVariableFactorParameterNames": [
//           "Ethnic Group"
//         ],
//         "studyVariableFactorValues": [
//           "Malay"
//         ],
//         "studyVariableFactorUnits": null,
//         "studyVariableFactorUnitAccessions": null,
//         "createdBy": "nilshoffmann",
//         "updatedBy": "nilshoffmann",
//         "dateCreated": "2021-03-17T16:27:53.000+00:00",
//         "dateLastModified": "2021-03-17T16:27:53.000+00:00",
//         "revision": "_cCCBzvi--6",
//         "visibility": "PUBLIC",
//         "owners": [
//           "root",
//           "nilshoffmann"
//         ],
//         "score": 1
//       },
//       {
//         "id": "bfdbbe8f-685f-3ef4-8647-e4a54f80a8b4",
//         "collection": "lipidQuantity",
//         "transactionUuid": "solr-106",
//         "normalizedName": [
//           "PC 36:6"
//         ],
//         "lipidLevel": [
//           "SPECIES"
//         ],
//         "lipidCategory": [
//           "GP"
//         ],
//         "lipidClass": [
//           "PC"
//         ],
//         "lipidSpecies": [
//           "PC 36:6"
//         ],
//         "lipidMolecularSubSpecies": null,
//         "lipidStructuralSubSpecies": null,
//         "lipidIsomericSubSpecies": null,
//         "organismNames": [
//           "Homo sapiens"
//         ],
//         "organismAccessions": [
//           "NCBITaxon:9606"
//         ],
//         "tissueNames": [
//           "blood plasma"
//         ],
//         "tissueAccessions": [
//           "BTO:0000131"
//         ],
//         "cellTypeNames": null,
//         "cellTypeAccessions": null,
//         "diseaseNames": null,
//         "diseaseAccessions": null,
//         "sampleCustomNames": [
//           "Institutional Review Board",
//           "Current Smoker",
//           "Age",
//           "Gender",
//           "Performing Laboratory",
//           "Body Mass Index"
//         ],
//         "sampleCustomAccessions": [
//           "NCIT:C16741",
//           "NCIT:C67147",
//           "NCIT:C25150",
//           "NCIT:C17357",
//           "NCIT:C64206",
//           "NCIT:C16358"
//         ],
//         "sampleCustomValues": [
//           "National Health Group Institutional Review Board (IRB 10-434), Singapore",
//           "1.0",
//           "46.0",
//           "Male",
//           "Baker IDI, Melbourne",
//           "29.01"
//         ],
//         "assayCustomNames": null,
//         "assayCustomAccessions": null,
//         "assayCustomValues": null,
//         "goMolecularFunctionNames": null,
//         "goMolecularFunctionAccessions": null,
//         "goCellComponentNames": null,
//         "goCellComponentAccessions": null,
//         "goBiologicalProcessNames": null,
//         "goBiologicalProcessAccessions": null,
//         "mzTabResultId": "LCDS1",
//         "smlId": 70,
//         "assayId": 234,
//         "studyVariableId": 1,
//         "assayQuantity": 120.0176,
//         "quantificationUnitAccession": "UO:0000072",
//         "quantificationUnitName": "picomolal",
//         "identificationReliability": "3",
//         "bestIdentificationConfidenceMeasureAccession": "MS:1002890",
//         "bestIdentificationConfidenceMeasureName": "fragmentation score",
//         "bestIdConfidenceValue": 1,
//         "studyVariableFactorNativeIds": null,
//         "studyVariableFactorParameterAccessions": [
//           "NCIT:C16564"
//         ],
//         "studyVariableFactorParameterNames": [
//           "Ethnic Group"
//         ],
//         "studyVariableFactorValues": [
//           "Indian"
//         ],
//         "studyVariableFactorUnits": null,
//         "studyVariableFactorUnitAccessions": null,
//         "createdBy": "nilshoffmann",
//         "updatedBy": "nilshoffmann",
//         "dateCreated": "2021-03-17T16:27:53.000+00:00",
//         "dateLastModified": "2021-03-17T16:27:53.000+00:00",
//         "revision": "_cCCBzvi--8",
//         "visibility": "PUBLIC",
//         "owners": [
//           "root",
//           "nilshoffmann"
//         ],
//         "score": 1
//       }
//     ],
//     "pageable": {
//       "sort": {
//         "sorted": false,
//         "unsorted": true,
//         "empty": true
//       },
//       "offset": 0,
//       "pageNumber": 0,
//       "pageSize": 5,
//       "paged": true,
//       "unpaged": false
//     },
//     "facetResultPages": [
//       {
//         "content": [
//           {
//             "valueCount": 58158,
//             "value": "GP",
//             "field": {
//               "name": "lipidCategory"
//             },
//             "key": {
//               "name": "lipidCategory"
//             }
//           }
//         ],
//         "pageable": {
//           "sort": {
//             "sorted": false,
//             "unsorted": true,
//             "empty": true
//           },
//           "offset": 0,
//           "pageNumber": 0,
//           "pageSize": 100,
//           "paged": true,
//           "unpaged": false
//         },
//         "facetResultPages": [],
//         "facetQueryResult": {
//           "content": [],
//           "pageable": "INSTANCE",
//           "totalPages": 1,
//           "totalElements": 0,
//           "last": true,
//           "size": 0,
//           "number": 0,
//           "sort": {
//             "sorted": false,
//             "unsorted": true,
//             "empty": true
//           },
//           "first": true,
//           "numberOfElements": 0,
//           "empty": true
//         },
//         "highlighted": [],
//         "maxScore": null,
//         "fieldStatsResults": {},
//         "suggestions": [],
//         "facetFields": [],
//         "facetPivotFields": [],
//         "allFacets": [
//           null
//         ],
//         "alternatives": [],
//         "totalPages": 1,
//         "totalElements": 1,
//         "size": 100,
//         "number": 0,
//         "sort": {
//           "sorted": false,
//           "unsorted": true,
//           "empty": true
//         },
//         "first": true,
//         "last": true,
//         "numberOfElements": 1,
//         "empty": false
//       },
//       {
//         "content": [
//           {
//             "valueCount": 58158,
//             "value": "PC",
//             "field": {
//               "name": "lipidClass"
//             },
//             "key": {
//               "name": "lipidClass"
//             }
//           }
//         ],
//         "pageable": {
//           "sort": {
//             "sorted": false,
//             "unsorted": true,
//             "empty": true
//           },
//           "offset": 0,
//           "pageNumber": 0,
//           "pageSize": 100,
//           "paged": true,
//           "unpaged": false
//         },
//         "facetResultPages": [],
//         "facetQueryResult": {
//           "content": [],
//           "pageable": "INSTANCE",
//           "totalPages": 1,
//           "totalElements": 0,
//           "last": true,
//           "size": 0,
//           "number": 0,
//           "sort": {
//             "sorted": false,
//             "unsorted": true,
//             "empty": true
//           },
//           "first": true,
//           "numberOfElements": 0,
//           "empty": true
//         },
//         "highlighted": [],
//         "maxScore": null,
//         "fieldStatsResults": {},
//         "suggestions": [],
//         "facetFields": [],
//         "facetPivotFields": [],
//         "allFacets": [
//           null
//         ],
//         "alternatives": [],
//         "totalPages": 1,
//         "totalElements": 1,
//         "size": 100,
//         "number": 0,
//         "sort": {
//           "sorted": false,
//           "unsorted": true,
//           "empty": true
//         },
//         "first": true,
//         "last": true,
//         "numberOfElements": 1,
//         "empty": false
//       }
//     ],
//     "facetQueryResult": {
//       "content": [],
//       "pageable": "INSTANCE",
//       "totalPages": 1,
//       "totalElements": 0,
//       "last": true,
//       "size": 0,
//       "number": 0,
//       "sort": {
//         "sorted": false,
//         "unsorted": true,
//         "empty": true
//       },
//       "first": true,
//       "numberOfElements": 0,
//       "empty": true
//     },
//     "highlighted": [],
//     "maxScore": 1,
//     "fieldStatsResults": {},
//     "suggestions": [],
//     "facetFields": [
//       {
//         "name": "lipidCategory"
//       },
//       {
//         "name": "lipidClass"
//       }
//     ],
//     "facetPivotFields": [
//       {
//         "fields": [
//           {
//             "name": "lipidCategory"
//           },
//           {
//             "name": "lipidClass"
//           },
//           {
//             "name": "lipidSpecies"
//           }
//         ],
//         "name": "lipidCategory,lipidClass,lipidSpecies"
//       }
//     ],
//     "allFacets": [
//       {
//         "content": [
//           {
//             "valueCount": 58158,
//             "value": "GP",
//             "field": {
//               "name": "lipidCategory"
//             },
//             "key": {
//               "name": "lipidCategory"
//             }
//           }
//         ],
//         "pageable": {
//           "sort": {
//             "sorted": false,
//             "unsorted": true,
//             "empty": true
//           },
//           "offset": 0,
//           "pageNumber": 0,
//           "pageSize": 100,
//           "paged": true,
//           "unpaged": false
//         },
//         "facetResultPages": [],
//         "facetQueryResult": {
//           "content": [],
//           "pageable": "INSTANCE",
//           "totalPages": 1,
//           "totalElements": 0,
//           "last": true,
//           "size": 0,
//           "number": 0,
//           "sort": {
//             "sorted": false,
//             "unsorted": true,
//             "empty": true
//           },
//           "first": true,
//           "numberOfElements": 0,
//           "empty": true
//         },
//         "highlighted": [],
//         "maxScore": null,
//         "fieldStatsResults": {},
//         "suggestions": [],
//         "facetFields": [],
//         "facetPivotFields": [],
//         "allFacets": [
//           null
//         ],
//         "alternatives": [],
//         "totalPages": 1,
//         "totalElements": 1,
//         "size": 100,
//         "number": 0,
//         "sort": {
//           "sorted": false,
//           "unsorted": true,
//           "empty": true
//         },
//         "first": true,
//         "last": true,
//         "numberOfElements": 1,
//         "empty": false
//       },
//       {
//         "content": [
//           {
//             "valueCount": 58158,
//             "value": "PC",
//             "field": {
//               "name": "lipidClass"
//             },
//             "key": {
//               "name": "lipidClass"
//             }
//           }
//         ],
//         "pageable": {
//           "sort": {
//             "sorted": false,
//             "unsorted": true,
//             "empty": true
//           },
//           "offset": 0,
//           "pageNumber": 0,
//           "pageSize": 100,
//           "paged": true,
//           "unpaged": false
//         },
//         "facetResultPages": [],
//         "facetQueryResult": {
//           "content": [],
//           "pageable": "INSTANCE",
//           "totalPages": 1,
//           "totalElements": 0,
//           "last": true,
//           "size": 0,
//           "number": 0,
//           "sort": {
//             "sorted": false,
//             "unsorted": true,
//             "empty": true
//           },
//           "first": true,
//           "numberOfElements": 0,
//           "empty": true
//         },
//         "highlighted": [],
//         "maxScore": null,
//         "fieldStatsResults": {},
//         "suggestions": [],
//         "facetFields": [],
//         "facetPivotFields": [],
//         "allFacets": [
//           null
//         ],
//         "alternatives": [],
//         "totalPages": 1,
//         "totalElements": 1,
//         "size": 100,
//         "number": 0,
//         "sort": {
//           "sorted": false,
//           "unsorted": true,
//           "empty": true
//         },
//         "first": true,
//         "last": true,
//         "numberOfElements": 1,
//         "empty": false
//       },
//       {
//         "content": [],
//         "pageable": "INSTANCE",
//         "totalPages": 1,
//         "totalElements": 0,
//         "last": true,
//         "size": 0,
//         "number": 0,
//         "sort": {
//           "sorted": false,
//           "unsorted": true,
//           "empty": true
//         },
//         "first": true,
//         "numberOfElements": 0,
//         "empty": true
//       }
//     ],
//     "alternatives": [],
//     "totalPages": 11632,
//     "totalElements": 58158,
//     "size": 5,
//     "number": 0,
//     "sort": {
//       "sorted": false,
//       "unsorted": true,
//       "empty": true
//     },
//     "first": true,
//     "last": false,
//     "numberOfElements": 5,
//     "empty": false
//   }

export interface LipidQueryResponse {

}