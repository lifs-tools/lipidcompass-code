import { Component, Input } from '@angular/core';
import { MatomoTracker } from 'ngx-matomo-client';
import { environment } from '../../environments/environment';
import { Checkbox, CheckboxChangeEvent } from 'primeng/checkbox';
import { Messages } from 'primeng/messages';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-tracking-consent',
  templateUrl: './tracking-consent.component.html',
  styleUrls: ['./tracking-consent.component.css']
})
export class TrackingConsentComponent {

  // optedOut$: Promise<boolean>

  privacyUrl: string = environment.privacyUrl

  @Input() showStatus: boolean = false

  // optedIn: boolean = true

  constructor(private readonly tracker: MatomoTracker) {
    // this.optedOut$ = tracker.isUserOptedOut()
    // this.optedOut$.then((optedOutStatus) => {
    //   this.optedIn = !optedOutStatus
    // })
  }

  // handleChange(event: any) {
  //   console.debug(JSON.stringify(event))
  //   var optOutStatus = this.tracker.isUserOptedOut()
  //   optOutStatus.then((optedOutStatus) => {
  //     console.debug("optedOutStatus: " + optedOutStatus)
  //   })
  //   if (!event) {
  //     console.info("Opting out of tracking")
  //     this.tracker.optUserOut()
  //   } else {
  //     console.info("Opting in for tracking")
  //     this.tracker.forgetUserOptOut()
  //   }

  //   this.optedOut$ = this.tracker.isUserOptedOut()
  //   this.optedOut$.then((optedOutStatus) => {
  //     this.optedIn = !optedOutStatus
  //   })
  // }

}
