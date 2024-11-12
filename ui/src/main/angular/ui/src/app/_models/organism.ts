import { CrossReference } from './crossreference';

export interface Organism {
  id: string;
  createdBy: string;
  updatedBy: string;
  name: string;
  description: string;
  crossReferences: Array<CrossReference>;
  dateCreatedString: string;
  dateLastModified: string;
  _links: {
    self: {
      href: string;
    };
  };
}
