import { filter } from "lodash";
import { EntityModelMzTabResult, Lipid, PlottableLipidQuantity } from "../../../modules/lipidcompass-backend-client";
import { LipidQuery } from "../../../modules/lipidcompass-backend-client/model/lipidQuery";

export enum FILTER_TYPE {
    BOOLEAN,
    SELECT,
    RANGE
  }

export class Filter {
    name: string;
    description: string;
    type: FILTER_TYPE;
    constructor(name: string, description: string, type: FILTER_TYPE) {
        this.name = name;
        this.description = description;
        this.type = type;
    }
  
    apply(lipidQuery: LipidQuery): LipidQuery {
      return lipidQuery;
    }
  }

export class LipidLevelFilter extends Filter {

    private lipidLevel: Lipid.LipidLevelEnum = Lipid.LipidLevelEnum.CATEGORY;

    constructor(lipidLevel: Lipid.LipidLevelEnum) {
        super("Lipid Level", "Select lipids based on their lipid shorthand level.", FILTER_TYPE.SELECT);
        this.lipidLevel = lipidLevel;
    }
  
    apply(lipidQuery: LipidQuery): LipidQuery {
      const filteredQuery = {
        ...lipidQuery,
        lipidLevel: this.lipidLevel
      };
      return <LipidQuery>filteredQuery;
    }
  }