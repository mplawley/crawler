import { Moment } from 'moment';

export interface ICrawl {
  id?: number;
  time?: Moment;
  url?: string;
  result?: string;
}

export class Crawl implements ICrawl {
  constructor(public id?: number, public time?: Moment, public url?: string, public result?: string) {}
}
