import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {GameOverview} from "../../../game/model/game-overview";

@Component({
    selector: 'dialog-delete',
    template: `
        <h2 mat-dialog-title>{{data.game.name}} Löschen</h2>
        <mat-dialog-content class="mat-typography">
            <p>
                Wenn du diesen MUD löschst, werden alle Spieler gekickt und der aktuelle Spielstand gelöscht. <br>
                Dieser kann nicht wiederhergestellt werden!
            </p>

        </mat-dialog-content>
        <mat-dialog-actions align="end">
            <button mat-button mat-dialog-close>Abbrechen</button>
            <button mat-button color="warn" [mat-dialog-close]="true" cdkFocusInitial>Endgültig löschen</button>
        </mat-dialog-actions>
    `
})
export class DeleteDialog {
    constructor(@Inject(MAT_DIALOG_DATA) public data: { game: GameOverview }) {
    }

}
