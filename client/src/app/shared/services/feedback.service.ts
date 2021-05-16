import {Component, Inject, Injectable} from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material/dialog";
import {Overlay} from "@angular/cdk/overlay";
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
    try {
      this.loadingOverlay.attach(new ComponentPortal(MatSpinner))
    } catch (e) {
    }
  }

  stopLoadingOverlay() {
    this.loadingOverlay.detach()
  }

  showError(error: HttpErrorResponse) {
    const errorMessage = typeof error.error === "string"
      ? error.error
      : (console.error(error), "Eine detaillierte Fehlermeldung findest du in der Konsole. Wende dich damit bitte an den Entwickler.")
    this.alert({
      title: "Etwas ist schiefgelaufen",
      message: errorMessage
    })
  }

  alert(data: DialogData) {
    this.dialog.open(Dialog, {
      data: {
        primaryAction: ["Ok", () => null],
        ...data
      }
    });
  }
}

type DialogData = {
  title: string,
  message: string,
  primaryAction?: [string, () => void],
  secondaryAction?: [string, () => void],
}

@Component({
  selector: 'dialog-delete',
  template: `
    <h2 mat-dialog-title>{{data.title}}</h2>
    <mat-dialog-content class="mat-typography">
      <p>{{data.message}}</p>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button color="primary" mat-dialog-close *ngIf="data.primaryAction as action"
              (click)="action[1]()">{{action[0]}}</button>
      <button mat-button mat-dialog-close *ngIf="data.secondaryAction as action"
              (click)="action[1]()">{{action[0]}}</button>
    </mat-dialog-actions>
  `
})
export class Dialog {
  constructor(@Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

}
