import {Component, Inject} from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {AuthService} from "../../../authentication/services/auth.service";

@Component({
  selector: './change-password-dialog',
  templateUrl: './change-password-dialog.html',
  styleUrls: ['./change-password-dialog.scss']
})

export class ChangePasswordDialog{

  mail : string | undefined;
  oldPassword : string | undefined;
  newPassword: string | undefined;

  constructor(public dialogRef: MatDialogRef<ChangePasswordDialog>, private authService: AuthService) {}


  cancel(){
    this.dialogRef.close();
  }

  submitNewPassword(){
    if(this.newPassword != undefined && this.oldPassword != undefined) {
      this.authService.changePassword(this.oldPassword, this.newPassword);
      this.dialogRef.close();
    }else{
      window.alert("Es wurden nicht alle Daten eingegeben.");
    }
  }
}
