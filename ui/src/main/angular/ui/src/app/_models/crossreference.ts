export interface CrossReference {
  id: string;
  createdBy: string;
  updatedBy: string;
  crossReferenceType: string;
  url: string;
  nativeId: string;
  dateCreatedString: string;
  dateLastModified: string;
  _links: {
    self: {
      href: string;
    };
  };
}
