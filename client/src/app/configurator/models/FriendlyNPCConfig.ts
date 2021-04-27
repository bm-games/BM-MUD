import {NPCConfig} from "./NPCConfig";

export class FriendlyNPCConfig extends NPCConfig{
  private _commandOnInteraction: string;
  private _messageOnTalk: string;

  constructor(id: number, name: string, items: number[], loottable: number[], commandOnInteraction: string, messageOnTalk: string) {
    super(id, name, 'Verbündet', items, loottable);
    this._commandOnInteraction = commandOnInteraction;
    this._messageOnTalk = messageOnTalk;
  }

  get messageOnTalk(): string {
    return this._messageOnTalk;
  }

  set messageOnTalk(value: string) {
    this._messageOnTalk = value;
  }
  get commandOnInteraction(): string {
    return this._commandOnInteraction;
  }

  set commandOnInteraction(value: string) {
    this._commandOnInteraction = value;
  }
}
