import {Inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../model/user";
import {BehaviorSubject, Observable} from "rxjs";
import {ClientConfig, CONFIG} from "../../client-config";

const USER_IDENTIFIER = "UserIdentifier";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly user$: BehaviorSubject<User | null>

  get user(): Observable<User | null> {
    return this.user$.asObservable()
  }

  constructor(private http: HttpClient,
              @Inject(CONFIG) private CONFIG: ClientConfig) {
    const storedUser = localStorage.getItem(USER_IDENTIFIER);
    this.user$ = new BehaviorSubject(storedUser ? JSON.parse(storedUser) : null);
    this.user$.subscribe(user => localStorage.setItem(USER_IDENTIFIER, JSON.stringify(user)))
  }

  isLoggedIn(): boolean {
    return !!this.user$.value
  }

  login(credentials: { email: string, password: string }): Promise<void> {
    // this.user$.next({username: credentials.email})
    return this.http.post<User>(`${this.CONFIG.endpoint}/auth/login`, credentials)
      .toPromise()
      .then(user => this.user$.next(user))
  }

  register(credentials: { email: string, username: string, password: string }): Promise<void> {
    // this.user$.next({username: credentials.username})
    return this.http.post<void>(`${this.CONFIG.endpoint}/auth/register`, credentials)
      .toPromise()
  }

  resetPassword(email: string): Promise<void> {
    return this.http.post<void>(`${this.CONFIG.endpoint}/auth/reset`, {email}).toPromise()
  }

  logout(): Promise<void> {
    this.user$.next(null)
    return this.http.get<void>(`${this.CONFIG.endpoint}/auth/logout`).toPromise()
  }

  changePassword(oldPassword: string, newPassword: string): Promise<void>{
    return this.http.post<void>(`${this.CONFIG.endpoint}/auth/changePassword`, {oldPassword, newPassword}).toPromise()
    console.log('change PW')

  }
}
