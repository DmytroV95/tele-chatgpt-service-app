import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {UserAdminChatResponse} from "../dto/user-admin-chat-response";
import {UserAdminChatService} from "../service/user-admin-chat.service";
import {TelegramMessageRequest} from "../dto/telegram-message-request";
import {TelegramMessengerService} from "../service/telegram-messenger.service";
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from "rxjs";

@Component({
    selector: 'app-user-admin-chat',
    templateUrl: './user-admin-chat.component.html',
    styleUrls: ['./user-admin-chat.component.css']
})
export class UserAdminChatComponent implements OnInit, OnDestroy {
    @ViewChild('scrollContainer') private scrollContainer: ElementRef | undefined;
    chatLogs: UserAdminChatResponse[] = [];
    selectedChatId: bigint | null;
    uniqueChatLogs: UserAdminChatResponse[];
    messageRequest: TelegramMessageRequest = {
        chatId: '',
        message: '',
    };
    private pollSubscription: Subscription | undefined;

    constructor(
        public userAdminChatService: UserAdminChatService,
        private router: Router,
        private telegramService: TelegramMessengerService,
        private route: ActivatedRoute) {
        this.uniqueChatLogs = [];
        this.chatLogs = [];
        this.selectedChatId = null;
    }

    ngOnInit(): void {
        this.route.params.subscribe(params => {
            if (params['chatId']) {
                this.selectedChatId = params['chatId'];
                if (this.selectedChatId !== null) {
                    this.loadChatLogsById(this.selectedChatId);
                }
            }
        });

        this.loadChatLogs();
        setInterval(() => {
            if (this.selectedChatId !== null) {
                this.loadChatLogsById(this.selectedChatId);
                this.scrollToBottom();
            }
        }, 0.3 * 60 * 1000);
    }

    ngOnDestroy(): void {
        if (this.pollSubscription) {
            this.pollSubscription.unsubscribe();
        }
    }

    loadChatLogs() {
        this.userAdminChatService.getAllChatLogs().subscribe(
            (chatLogs) => {
                this.chatLogs = chatLogs;
                console.log('Chat Logs:', this.chatLogs);

                this.uniqueChatLogs = this.chatLogs.filter(
                    (log, index, self) =>
                        index === self.findIndex((t) =>
                            t.telegramChatId === log.telegramChatId)
                );
                if (this.selectedChatId !== null && this.selectedChatId !== undefined) {
                    this.router.navigate(['/user-admin-chat', this.selectedChatId]);
                }
            },
            (error) => {
                console.error('Error loading chat logs:', error);
            }
        );
    }

    loadChatLogsById(chatId: bigint | null) {
        console.log("Invoke loadChatLogsById");
        this.selectedChatId = chatId;
        if (this.selectedChatId !== null) {
            this.userAdminChatService.getChatLogsById(this.selectedChatId.toString()).subscribe(
                (chatLogs) => {
                    this.chatLogs = chatLogs;
                },
                (error) => {
                    console.error('Error loading chat logs by ID:', error);
                }
            );
        }
    }

    sendMessage(): void {
        if (this.selectedChatId !== null) {
            console.log('Telegram Chat ID:', this.messageRequest.chatId);
            console.log('Message:', this.messageRequest.message);
            this.messageRequest.chatId = this.selectedChatId.toString();
            this.telegramService.sendMessageToTelegram(this.messageRequest).subscribe(
                (response) => {
                    this.loadChatLogsById(this.selectedChatId)
                    console.log('Message sent successfully:', response);
                },
                (error) => {
                    console.error('Error sending message:', error);
                }
            ).add(() => {
                this.messageRequest.message = '';
            });
        }
    }

    private scrollToBottom(): void {
        if (this.scrollContainer) {
            const container = this.scrollContainer.nativeElement;
            container.scrollTop = container.scrollHeight;
        }
    }
}
