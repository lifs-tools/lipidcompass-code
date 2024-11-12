import {Injectable} from '@angular/core';
import { Subject } from 'rxjs';

@Injectable()
export class SelectionService<T> {

    private selectedItem = new Subject<T>();

    selection$ = this.selectedItem.asObservable();

    select(selectedItem: T) {
        this.selectedItem.next(selectedItem);
    }

    reset() {
        this.selectedItem.next(undefined);
    }
}
