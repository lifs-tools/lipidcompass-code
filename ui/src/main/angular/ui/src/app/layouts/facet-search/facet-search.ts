import { JsonpInterceptor } from "@angular/common/http";
import { MaxLengthValidator } from "@angular/forms";
import { SortableColumn } from "primeng/table";
import { logging } from "selenium-webdriver";
import { Page } from "../../_models/page";

export class FacetQuery {
  defaultOperator: QueryOperator = QueryOperator.OR;
  defType: string;
  filterQueries: FilterQuery[];
  groupByFields: Field[];
  groupOptions: GroupOptions;
  offset: number;
  rows: number;
  projectionOnFields: Field[];
  sort: Sort;
  spellcheckOptions: SpellcheckOptions;
  statsOptions: StatsOptions;
  timeAllowed: number;
}

export class StatsOptions {}

export class SpellcheckOptions {}

export class Sort {}

export class GroupOptions {}

export enum QueryOperator {
  AND,
  NONE,
  OR,
}
export class FilterQuery {
  criteria: Criteria;
  join: Join;
}

export class Join {
  from: Field;
  to: Field;
  fromIndex: string;
}

export class Criteria {}

export class Field {
  name: string;
}

export interface FacetQuery {
  query: string;
  filterQuery: string;
  facetOptions: FacetOptions;
}

export interface FacetOptions {}

export interface FacetField {
  name: string;
}

export interface FacetPivotField {
  name: string;
}

export interface PivotField extends Field {
  fields: Field[];
}

// export interface Page<T> {
//   totalElements: number;
//   totalPages: number;
//   content: T[];
//   number: number;
//   numberOfElements: number;
//   pageable: Pageable;
//   size: number;
//   sort: Sort;
//   hasNext: boolean;
//   hasPrevious: boolean;
//   first: boolean;
//   last: boolean;
//   empty: boolean;
// }

export interface CountEntry {
  value: string;
  valueCount: number;
}

export interface FacetEntry extends CountEntry {
  key: any;
}

export interface FacetFieldEntry extends CountEntry {
  field: Field;
  key: Field;
}

export interface FacetPivotFieldEntry extends FacetFieldEntry {
  pivot: FacetPivotFieldEntry[];
}

export interface FacetQueryEntry extends FacetEntry {
  key: string;
  query: FilterQuery;
}

export interface TermsEntry extends CountEntry {
  key: any;
}

export interface TermsFieldEntry extends TermsEntry {
  key: Field;
}

export class StatsResult {
  count: number;
  max: any;
  mean: any;
  min: any;
  missing: number;
  stddev: number;
  sum: any;
  sumOfSquares: number;

  getMeanAsNumber(): number {
    return Number(this.mean) || NaN;
  }

  getMinAsNumber(): number {
    return Number(this.min) || NaN;
  }

  getMaxAsNumber(): number {
    return Number(this.max) || NaN;
  }

  getSumAsNumber(): number {
    return Number(this.sum) || NaN;
  }
}

export interface FieldStatsResult extends StatsResult {
  distinctCount: number;
  distinctValues: any[];
  facetStatsResults: Map<string, Map<string, StatsResult>>;
}

export interface FacetQueryResult<T> {
  allFacets: Page<FacetEntry>[];
  facetFields: Field[];
  facetPivotFields: PivotField[];
  facetQueryResult: Page<FacetQueryEntry>;
  facetResultPages: Page<FacetFieldEntry>[];
  pivot: FacetPivotFieldEntry[];
  rangeFacetResultPages: Page<FacetFieldEntry>[];
}

export interface ScoredPage<T> extends Page<T> {
    maxScore: number;
}

export interface GroupEntry<T> {
    groupValue: string;
    result: Page<T>;
}

export interface GroupResult<T> {
    groupEntries: Page<GroupEntry<T>>;
    groupsCount: number;
    matches: number;
    name: string;
} 

export interface GroupPage<T> extends Page<T> {
    groupResults: Map<any, GroupResult<T>>;
}

export interface FacetPage<T> extends FacetQueryResult<T>, Page<T> {

}

export interface StatsPage<T> extends Page<T> {
    fieldStatsResults: Map<string, FieldStatsResult>;
}

export interface Pageable {
  offset: number;
  pageNumber: number;
  pageSize: number;
  sort: Sort;
  paged: boolean;
  unpaged: boolean;
}

export interface SolrResultPage<T> extends Page<T>, FacetPage<T>, FacetQueryResult<T>, GroupPage<T>, ScoredPage<T>, StatsPage<T> {
  fieldStatsResults: Map<string, FieldStatsResult>;
  suggestions: string[];
}
