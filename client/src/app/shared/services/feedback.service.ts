import {Component, Inject, Injectable} from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material/dialog";
import {FullscreenOverlayContainer, Overlay} from "@angular/cdk/overlay";
import {ComponentPortal} from "@angular/cdk/portal";
import {MatSpinner} from "@angular/material/progress-spinner";

@Injectable({
  providedIn: 'root'
})
export class FeedbackService {

  private loadingOverlay = this.overlay.create({
    hasBackdrop: true,
    // backdropClass: 'dark-backdrop',
    positionStrategy: this.overlay.position()
      .global()
      .centerHorizontally()
      .centerVertically()
  })

  constructor(private dialog: MatDialog,
              private overlay: Overlay) {
  }

  showLoadingOverlay() {
    this.loadingOverlay.attach(new ComponentPortal(MatSpinner))
  }

  stopLoadingOverlay() {
    this.loadingOverlay.detach()
  }

  showError(error: HttpErrorResponse) {
    this.dialog.open(ErrorDialog, {data: {error: error}});
  }
}

@Component({
  selector: 'dialog-delete',
  template: `
    <h2 mat-dialog-title>Etwas ist schiefgelaufen</h2>
    <mat-dialog-content class="mat-typography">
      <p>{{errorMessage}}</p>

    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close>Ok</button>
    </mat-dialog-actions>
  `
})
export class ErrorDialog {
  errorMessage: string;

  constructor(@Inject(MAT_DIALOG_DATA) public data: { error: HttpErrorResponse }) {
    if (typeof data.error.error === "string") {
      this.errorMessage = data.error.error
    } else {
      this.errorMessage = "Eine detaillierte Fehlermeldung findest du in der Konsole. Wende dich damit bitte an den Entwickler."
      console.error(data.error)
    }
  }

}
