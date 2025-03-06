document.addEventListener("DOMContentLoaded", function () {
    const chatbotBtn = document.getElementById("chatbot-btn");
    const chatbotContainer = document.getElementById("chatbot-container");
    const closeChatbot = document.getElementById("close-chatbot");
    const sendMessageBtn = document.getElementById("send-message");
    const userMessageInput = document.getElementById("user-message");
    const chatbotMessages = document.getElementById("chatbot-messages");
    const inquireBtn = document.getElementById("inquire-btn");
    const chatbotInputSection = document.getElementById("chatbot-input");

let openMessageShown = false;

    // 챗봇 열기
    chatbotBtn.addEventListener("click", () => {
//        console.log("Button clicked");  // 클릭 시 로그 확인
//        chatbotContainer.classList.toggle("hidden");
//        console.log(chatbotContainer.classList);  // 클래스 상태 출력
    if (chatbotContainer.style.display === "none" || !chatbotContainer.style.display) {
        chatbotContainer.style.display = "flex"; // 챗봇 창을 보이게 설정

        if (!openMessageShown) {
            fetch("/api/v1/chatbot/open", {
                method: "POST"
            })
            .then(response => response.text())
            .then(botMessage => {
                // 챗봇 메시지 출력
                chatbotMessages.innerHTML += `<div class="message bot">${botMessage}</div>`;
                chatbotMessages.scrollTop = chatbotMessages.scrollHeight; // 스크롤 자동 내리기

                // 문의하기 버튼을 보이게 하고, 오픈 메시지 출력 후 표시된 메시지에 대한 처리가 끝났다면
                inquireBtn.style.display = "block";
                openMessageShown = true; // 메시지가 출력되었음을 표시
            })
            .catch(error => {
                chatbotMessages.innerHTML += `<div class="message bot error">에러 발생 😢</div>`;
                console.error("Error:", error);
            });
        }
        inquireBtn.style.display = "block"; // 최초 클릭 시 문의하기 버튼을 보이게 함
    } else {
        chatbotContainer.style.display = "none"; // 챗봇 창을 숨기기
    }
    });

//    // 문의하기 버튼 클릭 시 /api/v1/chatbot/open 요청
//    inquireBtn.addEventListener("click", () => {
//        fetch("/api/v1/chatbot/open", {
//            method: "POST"
//        })
//        .then(response => response.text())
//        .then(botMessage => {
//            // "문의하기" 버튼을 숨기고 챗봇 메시지 출력
//            inquireBtn.style.display = "none";
//            const chatbotMessages = document.getElementById("chatbot-messages");
//            chatbotMessages.innerHTML += `<div class="message bot">${botMessage}</div>`;
//            chatbotMessages.scrollTop = chatbotMessages.scrollHeight; // 스크롤 자동 내리기
//        })
//        .catch(error => {
//            const chatbotMessages = document.getElementById("chatbot-messages");
//            chatbotMessages.innerHTML += `<div class="message bot error">에러 발생 😢</div>`;
//            console.error("Error:", error);
//        });
//    });

    // 문의하기 버튼 클릭 시 채팅 입력창과 전송 버튼 보이기
    inquireBtn.addEventListener("click", () => {
        // 문의하기 버튼 숨기기
        inquireBtn.style.display = "none";

        // 채팅 입력창 및 전송 버튼 보이기
        chatbotInputSection.style.display = "flex";
    });

    // 챗봇 닫기
    closeChatbot.addEventListener("click", () => {
          chatbotContainer.style.display = "none"; // 챗봇 창을 숨기기
//        chatbotContainer.classList.add("hidden");
    });

    // 메시지 전송
    sendMessageBtn.addEventListener("click", () => sendMessage());
    userMessageInput.addEventListener("keypress", (event) => {
        if (event.key === "Enter") sendMessage();
    });

    function sendMessage() {
        const userMessage = userMessageInput.value.trim();
        if (!userMessage) return;

        // 사용자 메시지 UI에 추가
        chatbotMessages.innerHTML += `<div class="message user">${userMessage}</div>`;
        userMessageInput.value = "";

        // 서버로 AJAX 요청
        fetch("/api/v1/chatbot/send?message=" + encodeURIComponent(userMessage), {
            method: "POST"
        })
        .then(response => response.text())
        .then(botMessage => {
            chatbotMessages.innerHTML += `<div class="message bot">${botMessage}</div>`;
            chatbotMessages.scrollTop = chatbotMessages.scrollHeight; // 스크롤 자동 내리기
        })
        .catch(error => {
            chatbotMessages.innerHTML += `<div class="message bot error">에러 발생 😢</div>`;
            console.error("Error:", error);
        });
    }
});
