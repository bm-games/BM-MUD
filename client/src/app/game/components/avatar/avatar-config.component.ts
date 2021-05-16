import {Component, Inject, Input, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {RaceConfig} from "../../../configurator/models/RaceConfig";
import {ClassConfig} from "../../../configurator/models/ClassConfig";
import {FeedbackService} from "../../../shared/services/feedback.service";
import {GameService} from "../../services/game.service";
import {ConfigService} from "../../../configurator/services/config.service";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {GameOverview} from "../../model/game-overview";
import {GameDetail} from "../../model/game-detail";

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

  constructor(@Inject(MAT_DIALOG_DATA) private detail: GameDetail,
              private fb: FormBuilder,
              private feedback: FeedbackService,
              private gameService: GameService) {
    this.races = detail.races
    this.classes = detail.classes
  }

  submitAvatar(): void {

  }

  ngOnInit(): void {
    this.form.valueChanges.subscribe(({race, clazz}: { race: RaceConfig, clazz: ClassConfig }) => {
      this.damage = race?.damageModifier * clazz?.damage
      this.health = clazz?.healthMultiplier * race?.health
    })
  }

}
