import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {RaceConfig} from "../../../configurator/models/RaceConfig";
import {ClassConfig} from "../../../configurator/models/ClassConfig";
import {FeedbackService} from "../../../shared/services/feedback.service";
import {GameService} from "../../services/game.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AvatarConfig, GameDetail} from "../../model/game-detail";

@Component({
  selector: 'app-avatarconfigurator',
  templateUrl: './avatar-config.component.html',
  styleUrls: ['./avatar-config.component.scss']
})
export class AvatarConfigComponent implements OnInit {

  races!: RaceConfig[];
  classes!: ClassConfig[];

  damage?: number;
  health?: number;

  readonly form = this.fb.group({
    name: [null, [Validators.required, Validators.minLength(2)]],
    race: [null, Validators.required],
    clazz: [null, Validators.required],
  });

  constructor(@Inject(MAT_DIALOG_DATA) private detail: GameDetail & { name: string },
              public ref: MatDialogRef<AvatarConfigComponent>,
              private fb: FormBuilder,
              private feedback: FeedbackService,
              private gameService: GameService) {
    this.races = detail.races
    this.classes = detail.classes
  }

  submitAvatar(): void {
    this.feedback.showLoadingOverlay()
    this.gameService.createAvatar(this.detail.name, this.form.value as AvatarConfig)
      .then(() => this.ref.close(true))
      .catch(error => this.feedback.showError(error))
      .finally(() => this.feedback.stopLoadingOverlay())
  }

  ngOnInit(): void {
    this.form.valueChanges.subscribe(({race, clazz}: { race: RaceConfig, clazz: ClassConfig }) => {
      this.damage = race?.damageModifier * clazz?.damage
      this.health = clazz?.healthMultiplier * race?.health
    })
  }

}
