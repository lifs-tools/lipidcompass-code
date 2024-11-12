import { EntityModelMzTabResult } from "../../../modules/lipidcompass-backend-client";

export class Correction {
    name: string;
    description: string;
    constructor(name: string, description: string) {
        this.name = name;
        this.description = description;
    }
  
    apply(datasets: EntityModelMzTabResult[]): any {
      return datasets;
    }
  }