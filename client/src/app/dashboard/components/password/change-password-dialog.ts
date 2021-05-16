import {Component} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {AuthService} from "../../../authentication/services/auth.service";
import {FormBuilder, FormControl, Validators} from "@angular/forms";
import {FeedbackService} from "../../../shared/services/feedback.service";
import {GameService} from "../../../game/services/game.service";

@Component({
  selector: './change-password-dialog',
  templateUrl: './change-password-dialog.html',
  styleUrls: ['./change-password-dialog.scss']
})

export class ChangePasswordDialog {

  readonly form = this.fb.group({
    old: [null, Validators.required],
    new: [null, Validators.required],
    new2: [null, [Validators.required, (control: FormControl) => this.isMatch(control.value) ? null : {mismatch: true}]],
  });


  constructor(private fb: FormBuilder,
              private feedback: FeedbackService,
              private gameService: GameService,
              public dialogRef: MatDialogRef<ChangePasswordDialog>,
              private authService: AuthService) {
  }

  isMatch(value: any): boolean {
    return value == this.form?.value?.new
  }

  cancel() {
    this.dialogRef.close();
  }

  submitNewPassword() {
    this.feedback.showLoadingOverlay()
    this.authService.changePassword(this.form.value.old, this.form.value.new)
      .then(() => this.dialogRef.close(true))
      .catch(error => this.feedback.showError(error))
      .finally(() => this.feedback.stopLoadingOverlay())
  }
}
