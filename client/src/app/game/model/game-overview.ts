export interface GameOverview {
  readonly name: string;
  readonly onlinePlayers: number;
  readonly masterOnline: boolean;
  readonly avatarCount: number;
  readonly userPermitted: "No" | "Yes" | "Pending";
  readonly isMaster: boolean;
}
