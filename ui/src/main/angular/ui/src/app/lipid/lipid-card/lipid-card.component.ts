import {
  ChangeDetectorRef,
  Component,
  ElementRef,
  Input,
  OnChanges, SimpleChanges,
  ViewChild
} from "@angular/core";
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { TranslateService } from "@ngx-translate/core";
import {
  CollectionModelEntityModelMzTabResultsForLipid,
  Lipid,
  LipidControllerService,
  LipidMapsEntryControllerService,
  LipidQuantityControllerService,
  PageMetadata,
  SwissLipidsEntryControllerService,
} from "../../../../modules/lipidcompass-backend-client";
import { Pageable } from "../../../../modules/lipidcompass-backend-client/model/pageable";
import { paging } from "../../../environments/environment";
// import { MzTabResultsForLipid } from "../../_models/mztabresult";
// import { Page } from "../../_models/page";

@Component({
  selector: "app-lipid-card",
  templateUrl: "./lipid-card.component.html",
  styleUrls: ["./lipid-card.component.css"],
})
export class LipidCardComponent implements OnChanges {

  @Input() lipid: Lipid;

  lipidSvg: SafeHtml;
  mzTabResults: CollectionModelEntityModelMzTabResultsForLipid;
  pageable: Pageable = { ...paging.defaultPageable };

  @Input() title: string;
  @Input() hideLinks: boolean = false;

  @ViewChild("lipidSvgDiv") lipidSvgDiv: ElementRef;

  constructor(
    translate: TranslateService,
    private sanitizer: DomSanitizer,
    private lipidService: LipidControllerService,
    private lipidQuantityService: LipidQuantityControllerService,
    private ref: ChangeDetectorRef
  ) {}

  ngOnChanges(changes: SimpleChanges) {
    console.debug("Received onChanges: " + JSON.stringify(changes));
    for (const propName in changes) {
      if (changes.hasOwnProperty(propName)) {
        switch (propName) {
          case "lipid": {
            if (changes.lipid.currentValue) {
              console.debug(
                "Received change for lipid: " + changes.lipid.currentValue.id
              );
              this.loadSvg(changes.lipid.currentValue.id);
              this.loadIdentifiedIn(changes.lipid.currentValue.id);
            }
          }
        }
      }
    }
  }

  private loadIdentifiedIn(id: string) {
    this.lipidQuantityService.findMzTabResultsForLipid(id, this.pageable.page, this.pageable.size, this.pageable.sort).subscribe((results) => {
      this.mzTabResults = results;
      console.debug(JSON.stringify(this.mzTabResults));
      this.ref.markForCheck();
    });
  }

  private loadSvg(id: string) {
    this.lipidService.getSmilesSvg(id).subscribe((lipidSvg) => {
      if(lipidSvg !== null) {
        console.debug("lipidSvg is set!");
        var svgString = lipidSvg?.svg;
        var xmlDoctypeString =
          "<?xml version='1.0' encoding='UTF-8'?>\n<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n";
        var svgSubstring = svgString.substring(xmlDoctypeString.length);
        this.lipidSvg = this.sanitizer.bypassSecurityTrustHtml(svgSubstring);
        this.ref.markForCheck(); // do this to trigger a view update
      }
    });
  }
}
