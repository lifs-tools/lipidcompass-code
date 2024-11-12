import { Component, Input } from '@angular/core';
import { CvParameter, Parameter } from '../../../modules/lipidcompass-backend-client';

@Component({
  selector: 'app-olslink',
  templateUrl: './olslink.component.html',
  styleUrls: ['./olslink.component.css']
})
export class OlslinkComponent {

  @Input() cvParam: CvParameter;

  @Input() param: Parameter;

}
