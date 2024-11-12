import { Component, OnInit } from '@angular/core';

export class Facet<T> {
  name: string;
  facetField: string;
  facetValues: FacetValue<T>[];
  selectedFacetValue: FacetValue<T>;
  constructor(name: string, facetField: string, facetValues: FacetValue<T>[]) {
    this.name = name;
    this.facetField = facetField;
    this.facetValues = facetValues;
  }
}

export class FacetValue<T> {
  value: T;
  count: number;
  constructor(value: T, count: number) {
    this.value = value;
    this.count = count;
  }
}

export class StringFacet extends Facet<string> {
  constructor(name: string, facetField: string, facetValues: FacetValue<string>[]) {
    super(name, facetField, facetValues)
  }
}

export class NumberFacet extends Facet<number> {
  facetRangeMin: number;
  facetRangeMax: number;
  constructor(name: string, facetField: string, facetValues: FacetValue<number>[], facetRangeMin: number, facetRangeMax: number) {
    super(name, facetField, facetValues)
    this.facetRangeMin = facetRangeMin;
    this.facetRangeMax = facetRangeMax;
  }
}

@Component({
  selector: 'app-facet-search',
  templateUrl: './facet-search.component.html',
  styleUrls: ['./facet-search.component.css']
})
export class FacetSearchComponent implements OnInit {

  selectedFacets: Facet<any>[];
  facets: Facet<any>[] = [
    new StringFacet("Field 1", "field1", [new FacetValue<string>("a", 12), new FacetValue<string>("b",833), new FacetValue<string>("c",231)]),
    new StringFacet("Field 2", "field2", [new FacetValue<string>("Neptune", 2), new FacetValue<string>("Mars", 15), new FacetValue<string>("Sun",21)]),
    new NumberFacet("Numeric Field 3", "field3", [new FacetValue<number>(8971.23, 713), new FacetValue<number>( 9023.0321, 9724), new FacetValue<number>(2712.10, 882)], 2712.0, 9023.0321)
  ];

  constructor() { }

  ngOnInit() {
  }

}
