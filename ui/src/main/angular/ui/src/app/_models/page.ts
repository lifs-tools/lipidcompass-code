export interface PageAndSort {
  size: number;
  number: number;
}

export interface Page<T> {
  data: Array<T>;
  size: number;
  totalElements: number;
  totalPages: number;
  number: number;
}
