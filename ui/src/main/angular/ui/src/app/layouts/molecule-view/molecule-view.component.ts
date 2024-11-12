import { AfterViewInit, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
declare var Kekule: any;

@Component({
    selector: 'app-molecule-view',
    templateUrl: './molecule-view.component.html',
    styleUrls: ['./molecule-view.component.css']
})
export class MoleculeViewComponent implements OnChanges, AfterViewInit {

    // @ViewChild('kekuleCanvas', { read: ElementRef })
    // canvasElement: ElementRef;

    @Input()
    mol?: string;

    // /**
    //  * Supported values: 'mol' for mdl molfiles, 'cml' for chemical markup language files, 'smi' for SMILES.
    //  * Set [molFormat] = 'value' on the component to fix the format. The default value is 'mol'.
    //  *
    //  * DO NOT REMOVE THE STRING TYPE, THIS WILL LEAD TO PROBLEMS RESOLVING THE TYPE AT RUNTIME!
    //  */
    // @Input()
    // molFormat = 'mol';

    viewer: any;

    constructor() {}

    ngAfterViewInit() {
        // this.getOrCreateViewer();
    }

    ngOnChanges(changes: SimpleChanges) {
        // console.debug("Received simple change for model!");
        // this.updateCanvas(this.molFormat, changes['mol'].currentValue);
        // console.debug("Updated canvas!");
    }

    // getOrCreateViewer() {
    //     //Called after ngAfterContentInit when the component's view has been initialized. Applies to components only.
    //     //Add 'implements AfterViewInit' to the class.
    //     // Kekule.Indigo.enable(); // enable indigo for SMILES rendering
    //     Kekule.scriptSrcInfo = {'path' : '/assets/kekule/'}; 
    //     Kekule.Indigo.enable(
    //         function (err: any) { console.error(err); }
    //     );
    //     // console.debug("Creating mdl viewer!");
    //     const molCanvasDiv = this.canvasElement.nativeElement;
    //     if(!molCanvasDiv) {
    //         // console.debug("Div for canvas could not be found!");
    //         return undefined;
    //     } else {
    //         // console.debug("Retrieving widget!");
    //         this.viewer = Kekule.Widget.getWidgetOnElem(molCanvasDiv);
    //         if (!this.viewer) {
    //             this.viewer = new Kekule.ChemWidget.Viewer(molCanvasDiv);
    //             this.viewer.setDimension('400px', '200px');
    //             this.viewer.setRenderType(Kekule.Render.RendererType.R2D);
    //             this.viewer.setMoleculeDisplayType(Kekule.Render.Molecule2DDisplayType.SKELETAL);
    //             this.viewer.setPredefinedSetting('static');
    //         }
    //         // console.debug("Created mdl viewer!");
    //         return this.viewer;
    //     }
    // }

    // updateCanvas(format: string, cml: string) {
    //     if(cml !== null) {
    //         // console.debug("Updating mdl viewer!");
    //         const mol = Kekule.IO.loadFormatData(cml, format);
    //         // console.debug("GetOrCreate mdl viewer!");
    //         const viewer = this.getOrCreateViewer();
    //         if (viewer) {
    // //            console.debug('Found viewer object!');
    //             viewer.setChemObj(mol);
    //         } else {
    //             console.debug('Viewer object not found!');
    //         }
    //     }
    // }
}
