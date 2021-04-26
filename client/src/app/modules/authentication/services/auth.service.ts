import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../model/user";
import {BehaviorSubject, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly user$ = new BehaviorSubject<User | null>(null)

  get user(): Observable<User | null> {
    return this.user$.asObservable()
  }

  constructor(private http: HttpClient) {
    this.login()
  }

  isLoggedIn(): boolean {
    return !!this.user$.value
  }

  login(): Promise<void> {
    this.user$.next({username: "Dummy"})
    //TODO login on server
    return Promise.resolve()
  }

  logout(): Promise<void> {
    this.user$.next(null)
    //TODO logout on server
    return Promise.resolve()
  }
}
