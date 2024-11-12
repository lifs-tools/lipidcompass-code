import { Input, Component } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-svg-view',
  templateUrl: './svg-view.component.html',
  styleUrls: ['./svg-view.component.css']
})
export class SvgViewComponent {

  _svg: any;

  @Input()
  set svg(svg: any) {
    this._svg = this.sanitizer.bypassSecurityTrustHtml(svg);
  }

  get svg(): any { return this._svg; }

  constructor(private sanitizer: DomSanitizer) { }

}
