export interface UserAdminChatResponse {
  id: bigint;
  telegramChatId: bigint;
  adminId: bigint;
  message: string;
  messageTime: string;
  messageSource: string;
}
