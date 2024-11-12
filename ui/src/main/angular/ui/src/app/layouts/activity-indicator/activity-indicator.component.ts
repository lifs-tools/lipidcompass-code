import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import {MessageService} from 'primeng/api';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-activity-indicator',
  templateUrl: './activity-indicator.component.html',
  styleUrls: ['./activity-indicator.component.css']
})
export class ActivityIndicatorComponent implements OnInit, OnDestroy {
  @Input() imgPath = 'assets/img/';
  @Input() baseImgFileName = 'lipidcompass-base.png';
  @Input() roseImgFileName = 'lipidcompass-rose.png';

  private animate: boolean;

  constructor(private messageService: MessageService) {}

  ngOnInit() {

  }

  ngOnDestroy() {}
}
