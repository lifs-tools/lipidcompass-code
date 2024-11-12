import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';

export interface Publisher {
  topic: string;
  name: string;
}

export interface BackendMessage {
  level: string;
  title: string;
  content: string;
}

export interface ServiceMessage {
  level: string;
  title: string;
  content: string;
}

@Injectable()
export class NotificationService {

  constructor(private messageService: MessageService) {}

  public publish(topic: string, level: string, title: string, content: any): void {
    console.debug("Received message for publication via publish! topic="+topic+"; level="+level+"; title="+title+"; content="+JSON.stringify(content));
    this.messageService.add({ key: topic, severity: level, summary: title, detail: content });
  }

  public publishMessage(topic: string, message: ServiceMessage): void {
    console.debug("Received message for publication via publishMessage! topic="+topic+"; message="+JSON.stringify(message));
    this.messageService.add({ key: topic, severity: message.level, summary: message.title, detail: message.content });
  }
}
