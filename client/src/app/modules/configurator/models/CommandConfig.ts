import {BaseConfig} from "./BaseConfig";

export class CommandConfig extends BaseConfig{
  //private commands: Map<string, string>

  private _command: string;
  private _actions: string[];

  constructor(id: number, command: string, actions: string[]) {
    super(id);
    this._command = command;
    this._actions = actions;
  }

  get actions(): string[] {
    return this._actions;
  }

  set actions(value: string[]) {
    this._actions = value;
  }
  get command(): string {
    return this._command;
  }

  set command(value: string) {
    this._command = value;
  }
}
