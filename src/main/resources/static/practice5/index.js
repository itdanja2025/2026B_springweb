// 펼쳐진 댓글 창의 boardId를 기록하여 목록 갱신 시 상태 유지
const openCommentSet = new Set();

// 1. 게시글 목록 조회 및 피드 렌더링 (GET /api/board)
async function getPosts() {
    try {
        const res = await axios.get('/api/board');
        const feedContainer = document.querySelector('.feed-container');
        feedContainer.innerHTML = '';

        res.data.forEach(post => {
            const card = document.createElement('article');
            card.className = 'post-card';

            const commentList = post.comments || [];
            const initial = (post.author || '?').charAt(0).toUpperCase();
            const isOpen = openCommentSet.has(post.id);

            const commentsHtml = commentList.length > 0
                ? commentList.map(c => `
                    <div class="comment-item">
                        <div>
                            <div class="cmt-author">${c.author || ''}</div>
                            <div class="cmt-text">${c.content || ''}</div>
                        </div>
                        <button type="button" class="btn-del-sm" onclick="removeComment(${post.id}, ${c.id})">삭제</button>
                    </div>
                `).join('')
                : '<div style="text-align: center; color: var(--text-sub); font-size: 13px; padding: 10px 0;">첫 번째 댓글을 남겨보세요.</div>';

            card.innerHTML = `
                <div class="post-header">
                    <div class="author-info">
                        <div class="avatar">${initial}</div>
                        <div class="author-meta">
                            <span class="author-name">${post.author || ''}</span>
                            <span class="post-date">${post.createdAt || ''}</span>
                        </div>
                    </div>
                </div>
                <div class="post-content">${post.content || ''}</div>
                <div class="post-actions">
                    <button type="button" class="action-chip" onclick="toggleComments(${post.id}, this)">
                        💬 ${commentList.length}
                    </button>
                    <button type="button" class="action-chip delete" onclick="removePost(${post.id})">
                        삭제
                    </button>
                </div>
                <!-- 하단 댓글 드로어 (인라인 출력) -->
                <div class="comment-drawer ${isOpen ? 'open' : ''}" id="comments-${post.id}">
                    <div class="comment-items-box">${commentsHtml}</div>
                    <div class="comment-write-box">
                        <div class="input-row">
                            <input type="text" class="input-cmt-author" placeholder="닉네임">
                            <input type="password" class="input-cmt-password" placeholder="비밀번호">
                        </div>
                        <textarea class="input-cmt-content" placeholder="댓글 남기기..." style="min-height: 55px;"></textarea>
                        <div style="text-align: right;">
                            <button type="button" class="btn-submit" style="padding: 8px 18px; font-size: 13px;" onclick="addComment(${post.id}, this)">댓글 등록</button>
                        </div>
                    </div>
                </div>
            `;
            feedContainer.appendChild(card);
        });
    } catch (err) {
        console.error(err);
    }
}

// 2. 댓글 영역 토글
function toggleComments(boardId, btn) {
    const drawer = document.querySelector(`#comments-${boardId}`);
    if (!drawer) return;

    if (drawer.classList.contains('open')) {
        drawer.classList.remove('open');
        openCommentSet.delete(boardId);
    } else {
        drawer.classList.add('open');
        openCommentSet.add(boardId);
    }
}

// 3. 글 등록 (POST /api/board - Body)
async function writePost() {
    const payload = {
        author: document.querySelector('.input-post-author').value,
        password: document.querySelector('.input-post-password').value,
        content: document.querySelector('.input-post-content').value
    };

    try {
        await axios.post('/api/board', payload);
        document.querySelector('.input-post-author').value = '';
        document.querySelector('.input-post-password').value = '';
        document.querySelector('.input-post-content').value = '';
        closePostModal();
        await getPosts();
    } catch (err) {
        console.error(err);
    }
}

// 4. 글 삭제 (DELETE /api/board?id=1&password=1234 - Query String)
async function removePost(id) {
    const password = prompt('비밀번호를 입력하세요:');
    try {
        await axios.delete('/api/board', {
            params: { id, password }
        });
        await getPosts();
    } catch (err) {
        console.error(err);
    }
}

// 5. 댓글 등록 (POST /api/board/comments - Body)
async function addComment(boardId, btn) {
    const box = btn.closest('.comment-write-box');
    const payload = {
        boardId: boardId,
        author: box.querySelector('.input-cmt-author').value,
        password: box.querySelector('.input-cmt-password').value,
        content: box.querySelector('.input-cmt-content').value
    };

    try {
        await axios.post('/api/board/comments', payload);
        openCommentSet.add(boardId);
        await getPosts();
    } catch (err) {
        console.error(err);
    }
}

// 6. 댓글 삭제 (DELETE /api/board/comments?commentId=2&password=1234 - Query String)
async function removeComment(boardId, commentId) {
    const password = prompt('비밀번호를 입력하세요:');
    try {
        await axios.delete('/api/board/comments', {
            params: { commentId, password }
        });
        openCommentSet.add(boardId);
        await getPosts();
    } catch (err) {
        console.error(err);
    }
}

// 모달 제어 함수
function openPostModal() {
    document.querySelector('.modal-post').classList.add('active');
}

function closePostModal() {
    document.querySelector('.modal-post').classList.remove('active');
}

// 모달 배경 클릭 시 닫기
document.querySelector('.modal-post').addEventListener('click', (e) => {
    if (e.target === document.querySelector('.modal-post')) closePostModal();
});

// 초기 실행
getPosts(); 