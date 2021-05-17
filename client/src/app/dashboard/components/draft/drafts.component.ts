import {Component, Inject, Input, OnInit} from '@angular/core';
import {DungeonConfig} from "../../../configurator/models/DungeonConfig";
import {MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef} from "@angular/material/bottom-sheet";
import {Router} from "@angular/router";
import {ConfigurationComponent} from "../../../configurator/components/configuration/configuration.component";
import {ConfigService} from "../../../configurator/services/config.service";

@Component({
  selector: 'app-drafts',
  templateUrl: './drafts.component.html',
  styleUrls: ['./drafts.component.scss']
})
export class DraftsComponent implements OnInit {

  drafts: Partial<DungeonConfig>[];

  constructor(@Inject(MAT_BOTTOM_SHEET_DATA) data: Partial<DungeonConfig>[],
              private router: Router,
              private bottomSheetRef: MatBottomSheetRef<DraftsComponent>,
              private configService: ConfigService) {
    this.drafts = data
  }

  description(game: Partial<DungeonConfig>): string {
    return `
    Rassen: ${(game.races || []).map(race => race.name).join(", ")}\n
    Klassen: ${(game.classes || []).map(clazz => clazz.name).join(", ")}\n
    Räume: ${Object.keys(game.rooms || {}).join(", ")}\n
    NPCs: ${Object.keys(game.npcConfigs || {}).join(", ")}\n
    Items: ${Object.keys(game.itemConfigs || {}).join(", ")}
    `.replace(/^ +/gm, '')
  }

  ngOnInit(): void {
  }

  edit(draft: Partial<DungeonConfig>) {
    this.bottomSheetRef.dismiss()
    this.router.navigateByUrl("/configurator")
      .then(() => {
        ConfigurationComponent.setConfig(draft)
      })
  }

  delete(draft: Partial<DungeonConfig>) {
    this.bottomSheetRef.dismiss()
    this.configService.deleteDraft(draft.id)
  }
}
